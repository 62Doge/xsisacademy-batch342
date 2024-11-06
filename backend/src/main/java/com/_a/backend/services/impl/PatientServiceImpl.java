package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.PatientRequestDTO;
import com._a.backend.dtos.responses.PatientResponseDTO;
import com._a.backend.entities.Biodata;
import com._a.backend.entities.Customer;
import com._a.backend.entities.CustomerMember;
import com._a.backend.repositories.BiodataRepository;
import com._a.backend.repositories.CustomerMemberRepository;
import com._a.backend.repositories.CustomerRepository;
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
    ModelMapper modelMapper;

    @Override
    public List<PatientResponseDTO> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
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
        patientResponseDTO.setCustomerRelationId(customerMember.get().getCustomerRelationId());
        return patientResponseDTO;
    }

    // @Override
    // public Optional<PatientResponseDTO> findById(Long id) {
    // Optional<CustomerMember> customerMember =
    // customerMemberRepository.findById(id);
    // if (customerMember.isPresent()) {
    // Optional<PatientResponseDTO> patientResponseDTO;
    // Optional<Customer> customer =
    // customerRepository.findById(customerMember.get().getCustomerId());
    // Optional<Biodata> biodata =
    // biodataRepository.findById(customer.get().getBiodataId());
    // patientResponseDTO.setId(id);
    // patientResponseDTO.setParentBiodataId(customerMember.get().getParentBiodataId());
    // patientResponseDTO.setFullName(biodata.get().getFullname());
    // patientResponseDTO.setDob(customer.get().getDob());
    // patientResponseDTO.setGender(customer.get().getGender());
    // patientResponseDTO.setBloodGroupId(customer.get().getBloodGroupId());
    // patientResponseDTO.setRhesus(customer.get().getRhesus_type());
    // patientResponseDTO.setCustomerRelationId(customerMember.get().getCustomerRelationId());
    // return patientResponseDTO;
    // }
    // return Optional.empty();
    // }

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
        patientResponseDTO.setCustomerRelationId(savedCustomerMember.getCustomerRelationId());
        return patientResponseDTO;
    }

    // @Override
    // public PatientResponseDTO update(Long id, PatientRequestDTO
    // patientRequestDTO) {
    // Optional<CustomerMember> customerMember =
    // customerMemberRepository.findById(id);
    // if (customerMember.isPresent()) {
    // Optional<Customer> customer =
    // customerRepository.findById(customerMember.get().getCustomerId());
    // Optional<Biodata> biodata =
    // biodataRepository.findById(customer.get().getBiodataId());

    // biodata.get().setFullname(patientRequestDTO.getFullName());
    // Biodata updatedBiodata = biodataRepository.save(biodata);

    // customer.get().setDob(patientRequestDTO.getDob());
    // String patientBlood = patientRequestDTO.getBlood();
    // if ("A".equals(patientBlood)) {
    // customer.get().setBloodGroupId(1L);
    // } else if ("B".equals(patientBlood)) {
    // customer.get().setBloodGroupId(2L);
    // } else if ("O".equals(patientBlood)) {
    // customer.get().setBloodGroupId(3L);
    // } else if ("AB".equals(patientBlood)) {
    // customer.get().setBloodGroupId(4L);
    // }
    // customer.get().setRhesus_type(patientRequestDTO.getRhesus());
    // Customer updatedCustomer = customerRepository.save(customer);

    // String patientRelation = patientRequestDTO.getRelation();
    // if ("Diri Sendiri".equals(patientRelation)) {
    // customerMember.get().setCustomerRelationId(1L);
    // } else if ("Suami".equals(patientRelation)) {
    // customerMember.get().setCustomerRelationId(2L);
    // } else if ("Istri".equals(patientRelation)) {
    // customerMember.get().setCustomerRelationId(3L);
    // } else if ("Anak".equals(patientRelation)) {
    // customerMember.get().setCustomerRelationId(4L);
    // }
    // CustomerMember updatedCustomerMember =
    // customerMemberRepository.save(customerMember);
    // }
    // }

    @Override
    public PatientResponseDTO update(Long id, PatientRequestDTO patientRequestDTO) {
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

        existingCustomerMember.setParentBiodataId(patientRequestDTO.getParentBiodataId());
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
        patientResponseDTO.setCustomerRelationId(updatedCustomerMember.getCustomerRelationId());

        return patientResponseDTO;
    }

}
