package com.educative.app.hlv.service;

import com.educative.app.hlv.dto.CareerRequest;
import com.educative.app.hlv.dto.CareerResponse;
import com.educative.app.hlv.exceptions.BusinessException;
import com.educative.app.hlv.exceptions.ResourceNotFoundException;
import com.educative.app.hlv.mapper.CareerMapper;
import com.educative.app.hlv.models.Career;
import com.educative.app.hlv.repo.CareerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CareerServiceImp {
    private final CareerRepository careerRepository;
    private final CareerMapper careerMapper;

    @Transactional
    public List<CareerResponse> findAll() {
        return careerRepository.findAll()
                .stream()
                .map(careerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CareerResponse> findAllActive() {
        return careerRepository.findByActiveTrue()
                .stream()
                .map(careerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CareerResponse findById(Long id){
        Career career = findCareerOrThrow(id);
        return careerMapper.toResponse(career);
    }

    @Transactional
    public CareerResponse create(CareerRequest request) {

        if (careerRepository.existsByName(request.getName())) {
            throw new BusinessException("Ya existe una carrera con el nombre: " + request.getName());
        }
        if (careerRepository.existsByCode(request.getCode().toUpperCase())) {
            throw new BusinessException("Ya existe una carrera con el código: " + request.getCode());
        }

        Career career = careerMapper.toEntity(request);
        Career saved = careerRepository.save(career);
        return careerMapper.toResponse(saved);
    }

    @Transactional
    public CareerResponse update(Long id, CareerRequest request) {
        Career career = findCareerOrThrow(id);

        if (!career.getName().equalsIgnoreCase(request.getName())
                && careerRepository.existsByName(request.getName())) {
            throw new BusinessException("Ya existe una carrera con el nombre: " + request.getName());
        }

        careerMapper.updateEntity(career, request);
        Career saved = careerRepository.save(career);
        return careerMapper.toResponse(saved);
    }

    @Transactional
    public void deactivate(Long id) {
        Career career = findCareerOrThrow(id);
        career.setActive(false);
        careerRepository.save(career);
    }

    private Career findCareerOrThrow(Long id) {
        return careerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada con id: " + id));
    }

    @Transactional
    public Career findEntityById(Long id) {
        return findCareerOrThrow(id);
    }

}
