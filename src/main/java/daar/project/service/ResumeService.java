package daar.project.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import daar.project.document.Resume;
import daar.project.helper.Indices;
import daar.project.search.SearchRequestDTO;
import daar.project.search.SearchUtil;

@Service
public class ResumeService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(ResumeService.class);

    private final RestHighLevelClient client;

    @Autowired
    public ResumeService(RestHighLevelClient client) {
        this.client = client;
    }

    public List<Resume> search(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.RESUME_INDEX,
                dto
        );

        if (request == null) {
            LOG.error("Failed to build search request");
            return Collections.emptyList();
        }

        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            final SearchHit[] searchHits = response.getHits().getHits();
            final List<Resume> resumes = new ArrayList<>(searchHits.length);
            for (SearchHit hit : searchHits) {
            	resumes.add(
                        MAPPER.readValue(hit.getSourceAsString(), Resume.class)
                );
            }

            return resumes;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Boolean index(final Resume resume) {
        try {
            final String resumeAsString = MAPPER.writeValueAsString(resume);

            final IndexRequest request = new IndexRequest(Indices.RESUME_INDEX);
            request.id(resume.getId());
            request.source(resumeAsString, XContentType.JSON);

            final IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            return response != null && response.status().equals(RestStatus.OK);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    public Resume getById(final String resumeId) {
        try {
            final GetResponse documentFields = client.get(
                    new GetRequest(Indices.RESUME_INDEX, resumeId),
                    RequestOptions.DEFAULT
            );
            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null;
            }

            return MAPPER.readValue(documentFields.getSourceAsString(), Resume.class);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}