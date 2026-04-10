package com.educative.app.hlv.service;

import com.educative.app.hlv.dto.SubjectRequest;
import com.educative.app.hlv.dto.SubjectResponse;
import com.educative.app.hlv.enums.Role;
import com.educative.app.hlv.exceptions.BusinessException;
import com.educative.app.hlv.exceptions.ResourceNotFoundException;
import com.educative.app.hlv.mapper.SubjectMapper;
import com.educative.app.hlv.models.Career;
import com.educative.app.hlv.models.Subject;
import com.educative.app.hlv.models.User;
import com.educative.app.hlv.repo.SubjectRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final CareerServiceImp careerService;
    private final UserService userService;
    private final SubjectMapper subjectMapper;

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public List<SubjectResponse> findAll() {
        return subjectRepository.findAll()
                .stream()
                .map(subjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> findByCareer(Long careerId) {
        return subjectRepository.findByCareerId(careerId)
                .stream()
                .map(subjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> findByCareerAndSemester(Long careerId, Integer semester) {
        return subjectRepository.findByCareerIdAndSemester(careerId, semester)
                .stream()
                .map(subjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubjectResponse findById(Long id) {
        return subjectMapper.toResponse(findSubjectOrThrow(id));
    }

    @Transactional
    public SubjectResponse create(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode().toUpperCase())) {
            throw new BusinessException("Ya existe una materia con el código: " + request.getCode());
        }

        Career career = careerService.findEntityById(request.getCareerId());
        User teacher = loadTeacherIfNeeded(request.getTeacherId());

        Subject subject = subjectMapper.toEntity(request, career, teacher);
        return subjectMapper.toResponse(subjectRepository.save(subject));
    }

    @Transactional
    public SubjectResponse update(Long id, SubjectRequest request) {
        Subject subject = findSubjectOrThrow(id);

        // Validar código duplicado solo si cambió
        if (!subject.getCode().equalsIgnoreCase(request.getCode())
                && subjectRepository.existsByCode(request.getCode().toUpperCase())) {
            throw new BusinessException("Ya existe una materia con el código: " + request.getCode());
        }

        Career career = careerService.findEntityById(request.getCareerId());
        User teacher = loadTeacherIfNeeded(request.getTeacherId());

        subjectMapper.updateEntity(subject, request, career, teacher);
        Subject saved = subjectRepository.save(subject);
        entityManager.flush();
        entityManager.refresh(saved);

        return subjectMapper.toResponse(saved);
    }

    @Transactional
    public SubjectResponse assignTeacher(Long subjectId, Long teacherId) {
        Subject subject = findSubjectOrThrow(subjectId);
        User teacher = loadTeacherIfNeeded(teacherId);
        subject.setTeacher(teacher);
        Subject saved = subjectRepository.save(subject);
        entityManager.flush();
        entityManager.refresh(saved);
        return subjectMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public void deactivate(Long id) {
        Subject subject = findSubjectOrThrow(id);
        subject.setActive(false);
        subjectRepository.save(subject);
    }

    @Transactional
    public void delete(Long id) {
        Subject subjectId = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El id no existe"));
        subjectRepository.deleteById(subjectId.getId());

    }


    private User loadTeacherIfNeeded(Long teacherId) {
        if (teacherId == null) return null;

        User teacher = userService.findEntityById(teacherId);

        // Validar que el usuario asignado realmente sea un maestro
        if (!Role.TEACHER.equals(teacher.getRole())) {
            throw new BusinessException("El usuario con id " + teacherId + " no es un maestro");
        }
        return teacher;
    }

    public Subject findSubjectOrThrow(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada con id: " + id));
    }
}
