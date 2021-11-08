package daar.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daar.project.document.Resume;
import daar.project.search.SearchRequestDTO;
import daar.project.service.ResumeService;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    private final ResumeService service;

    @Autowired
    public ResumeController(ResumeService service) {
        this.service = service;
    }

    @PostMapping
    public void index(@RequestBody final Resume resume) {
        service.index(resume);
    }

    @GetMapping("/{id}")
    public Resume getById(@PathVariable final String id) {
        return service.getById(id);
    }

    @PostMapping("/search")
    public List<Resume> search(@RequestBody final SearchRequestDTO dto) {
        return service.search(dto);
    }
}