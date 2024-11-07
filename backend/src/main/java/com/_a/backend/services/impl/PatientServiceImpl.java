package com._a.backend.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.projections.PatientProjectionDto;
import com._a.backend.dtos.requests.PatientRequestDTO;
import com._a.backend.dtos.responses.PatientResponseDTO;
import com._a.backend.entities.Biodata;
import com._a.backend.entities.Customer;
import com._a.backend.entities.CustomerMember;
import com._a.backend.repositories.BiodataRepository;
import com._a.backend.repositories.CustomerMemberRepository;
import com._a.backend.repositories.CustomerRepository;
import com._a.backend.services.AuthService;
import com._a.backend.services.PatientService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PatientServiceImpl implements PatientService<PatientRequestDTO, PatientResponseDTO> {

    @Autowired
    BiodataRepository biodataRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMemberRepository customerMemberRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<PatientResponseDTO> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    public Page<PatientResponseDTO> findActivePages(Long parentBiodataId, Pageable pageable) {
        Page<PatientProjectionDto> projections = customerMemberRepository.findPatient(parentBiodataId, pageable);
        return projections.map(projection -> new PatientResponseDTO(
            projection.getId(),
            projection.getParentBiodataId(),
            projection.getFullName(),
            projection.getDob(),
            projection.getGender(),
            projection.getBloodGroupId(),
            projection.getRhesus(),
            projection.getHeight(),
            projection.getWeight(),
            projection.getCustomerRelationId()
        ));
    }

    public Page<PatientResponseDTO> findActivePagesByName(String name, Long parentBiodataId, Pageable pageable) {
        Page<PatientProjectionDto> projections = customerMemberRepository.findPatientByName(name, parentBiodataId, pageable);
        return projections.map(projection -> new PatientResponseDTO(
            projection.getId(),
            projection.getParentBiodataId(),
            projection.getFullName(),
            projection.getDob(),
            projection.getGender(),
            projection.getBloodGroupId(),
            projection.getRhesus(),
            projection.getHeight(),
            projection.getWeight(),
            projection.getCustomerRelationId()
        ));
    }

    public List<PatientResponseDTO> findByParentBiodataId(Long parentBiodataId) {
        List<PatientResponseDTO> patientResponseDTOs = new ArrayList<PatientResponseDTO>();
        List<CustomerMember> customerMembers = customerMemberRepository
                .findAllByParentBiodataIdAndIsDeleteFalse(parentBiodataId);
        for (CustomerMember customerMember : customerMembers) {
            Optional<Customer> customer = customerRepository.findById(customerMember.getCustomerId());
            Optional<Biodata> biodata = biodataRepository.findById(customer.get().getBiodataId());
            PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
            patientResponseDTO.setId(customerMember.getId());
            patientResponseDTO.setParentBiodataId(customerMember.getParentBiodataId());
            patientResponseDTO.setFullName(biodata.get().getFullname());
            patientResponseDTO.setDob(customer.get().getDob());
            patientResponseDTO.setGender(customer.get().getGender());
            patientResponseDTO.setBloodGroupId(customer.get().getBloodGroupId());
            patientResponseDTO.setRhesus(customer.get().getRhesus_type());
            patientResponseDTO.setHeight(customer.get().getHeight());
            patientResponseDTO.setWeight(customer.get().getWeight());
            patientResponseDTO.setCustomerRelationId(customerMember.getCustomerRelationId());
            patientResponseDTOs.add(patientResponseDTO);
        }
        return patientResponseDTOs;
    }

    @Override
    public PatientResponseDTO findById(Long id) {
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        Optional<CustomerMember> customerMember = customerMemberRepository.findById(id);
        Optional<Customer> customer = customerRepository.findById(customerMember.get().getCustomerId());
        Optional<Biodata> biodata = biodataRepository.findById(customer.get().getBiodataId());
        patientResponseDTO.setId(id);
        patientResponseDTO.setParentBiodataId(customerMember.get().getParentBiodataId());
        patientResponseDTO.setFullName(biodata.get().getFullname());
        patientResponseDTO.setDob(customer.get().getDob());
        patientResponseDTO.setGender(customer.get().getGender());
        patientResponseDTO.setBloodGroupId(customer.get().getBloodGroupId());
        patientResponseDTO.setRhesus(customer.get().getRhesus_type());
        patientResponseDTO.setHeight(customer.get().getHeight());
        patientResponseDTO.setWeight(customer.get().getWeight());
        patientResponseDTO.setCustomerRelationId(customerMember.get().getCustomerRelationId());
        return patientResponseDTO;
    }

    @Override
    public PatientResponseDTO save(Long parentBiodataId, PatientRequestDTO patientRequestDTO) {
        Biodata biodata = new Biodata();
        biodata.setFullname(patientRequestDTO.getFullName());
        Biodata savedBiodata = biodataRepository.save(biodata);

        Customer customer = new Customer();
        customer.setBiodataId(savedBiodata.getId());
        customer.setDob(patientRequestDTO.getDob());
        customer.setGender(patientRequestDTO.getGender());
        customer.setRhesus_type(patientRequestDTO.getRhesus());
        customer.setHeight(patientRequestDTO.getHeight());
        customer.setWeight(patientRequestDTO.getWeight());
        String patientBlood = patientRequestDTO.getBlood();
        if ("A".equals(patientBlood)) {
            customer.setBloodGroupId(1L);
        } else if ("B".equals(patientBlood)) {
            customer.setBloodGroupId(2L);
        } else if ("O".equals(patientBlood)) {
            customer.setBloodGroupId(3L);
        } else if ("AB".equals(patientBlood)) {
            customer.setBloodGroupId(4L);
        }
        Customer savedCustomer = customerRepository.save(customer);

        CustomerMember customerMember = new CustomerMember();
        customerMember.setParentBiodataId(parentBiodataId);
        customerMember.setCustomerId(savedCustomer.getId());
        String patientRelation = patientRequestDTO.getRelation();
        if ("Diri Sendiri".equals(patientRelation)) {
            customerMember.setCustomerRelationId(1L);
        } else if ("Suami".equals(patientRelation)) {
            customerMember.setCustomerRelationId(2L);
        } else if ("Istri".equals(patientRelation)) {
            customerMember.setCustomerRelationId(3L);
        } else if ("Anak".equals(patientRelation)) {
            customerMember.setCustomerRelationId(4L);
        }
        CustomerMember savedCustomerMember = customerMemberRepository.save(customerMember);
        patientRequestDTO.setId(savedCustomerMember.getId());
        patientRequestDTO.setParentBiodataId(parentBiodataId);

        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patientRequestDTO.getId());
        patientResponseDTO.setParentBiodataId(patientRequestDTO.getParentBiodataId());
        patientResponseDTO.setFullName(savedBiodata.getFullname());
        patientResponseDTO.setDob(savedCustomer.getDob());
        patientResponseDTO.setGender(savedCustomer.getGender());
        patientResponseDTO.setBloodGroupId(savedCustomer.getBloodGroupId());
        patientResponseDTO.setRhesus(savedCustomer.getRhesus_type());
        patientResponseDTO.setHeight(savedCustomer.getHeight());
        patientResponseDTO.setWeight(savedCustomer.getWeight());
        patientResponseDTO.setCustomerRelationId(savedCustomerMember.getCustomerRelationId());
        return patientResponseDTO;
    }

    @Override
    public PatientResponseDTO update(Long id, Long parentBiodataId, PatientRequestDTO patientRequestDTO) {
        CustomerMember existingCustomerMember = customerMemberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CustomerMember not found with ID " + id));
        Customer existingCustomer = customerRepository.findById(existingCustomerMember.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Customer not found with ID " + existingCustomerMember.getCustomerId()));
        Biodata existingBiodata = biodataRepository.findById(existingCustomer.getBiodataId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Biodata not found with ID " + existingCustomer.getBiodataId()));

        existingBiodata.setFullname(patientRequestDTO.getFullName());
        existingCustomer.setDob(patientRequestDTO.getDob());
        existingCustomer.setGender(patientRequestDTO.getGender());
        existingCustomer.setRhesus_type(patientRequestDTO.getRhesus());
        existingCustomer.setHeight(patientRequestDTO.getHeight());
        existingCustomer.setWeight(patientRequestDTO.getWeight());

        String patientBlood = patientRequestDTO.getBlood();
        if ("A".equals(patientBlood)) {
            existingCustomer.setBloodGroupId(1L);
        } else if ("B".equals(patientBlood)) {
            existingCustomer.setBloodGroupId(2L);
        } else if ("O".equals(patientBlood)) {
            existingCustomer.setBloodGroupId(3L);
        } else {
            existingCustomer.setBloodGroupId(4L);
        }

        existingCustomerMember.setParentBiodataId(parentBiodataId);
        String patientRelation = patientRequestDTO.getRelation();
        if ("Diri Sendiri".equals(patientRelation)) {
            existingCustomerMember.setCustomerRelationId(1L);
        } else if ("Suami".equals(patientRelation)) {
            existingCustomerMember.setCustomerRelationId(2L);
        } else if ("Istri".equals(patientRelation)) {
            existingCustomerMember.setCustomerRelationId(3L);
        } else {
            existingCustomerMember.setCustomerRelationId(4L);
        }

        biodataRepository.save(existingBiodata);
        customerRepository.save(existingCustomer);
        CustomerMember updatedCustomerMember = customerMemberRepository.save(existingCustomerMember);

        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(updatedCustomerMember.getId());
        patientResponseDTO.setParentBiodataId(patientRequestDTO.getParentBiodataId());
        patientResponseDTO.setFullName(existingBiodata.getFullname());
        patientResponseDTO.setDob(existingCustomer.getDob());
        patientResponseDTO.setGender(existingCustomer.getGender());
        patientResponseDTO.setBloodGroupId(existingCustomer.getBloodGroupId());
        patientResponseDTO.setRhesus(existingCustomer.getRhesus_type());
        patientResponseDTO.setHeight(existingCustomer.getHeight());
        patientResponseDTO.setWeight(existingCustomer.getWeight());
        patientResponseDTO.setCustomerRelationId(updatedCustomerMember.getCustomerRelationId());

        return patientResponseDTO;
    }

}
