package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.model.Phone;
import com.example.demo.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.PersonRepository;
import com.example.demo.model.PersonDTO;

import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "Access-Control-Allow-Origin: *")
public class PersonDBController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @RequestMapping(value = "/persons/db", method = RequestMethod.GET)
    public List<PersonDTO> getAllPersons() {

        List<PersonDTO> all = new ArrayList<>();
        List<Phone> phoneEntities;
        List<Person> persons = personRepository.findAll();
        List<Phone> phones = phoneRepository.findAll();
        Map<Long, List<Phone>> phonesMap = new HashMap<>();
        for (Phone phon : phones) {
//            List<Phone> phoneList = phonesMap.get(phon.getPersonId());
//            if (phoneList == null) {
//                phoneList = new ArrayList<>();
//                phonesMap.put(phon.getPersonId(), phoneList);
//            }
//
//            phoneList.add(phon);

            if (phonesMap.containsKey(phon.getPersonId())) {
                phoneEntities = phonesMap.get(phon.getPersonId());
                phoneEntities.add(phon);
                phonesMap.put(phon.getPersonId(), phoneEntities);
            } else {
                phoneEntities = new ArrayList<>();
                phoneEntities.add(phon);
                phonesMap.put(phon.getPersonId(), phoneEntities);
            }
        }

        for (Person per : persons) {

            PersonDTO personDTO = new PersonDTO();
            if (phonesMap.containsKey(per.getId())) {
                personDTO.setPhones(phonesMap.get(per.getId()));
            } else {
                List<Phone> phones1 = new ArrayList<>();
                personDTO.setPhones(phones1);
            }
            personDTO.setName(per.getName());

            personDTO.setId(per.getId());
            personDTO.setAddress(per.getAddress());
            personDTO.setEmail(per.getEmail());
            all.add(personDTO);

        }


//        for (Person per : persons) {
//            phoneEntities = new ArrayList<>();
//            for (Phone phon : phones) {
//                if (phon.getPersonId() == per.getId()) {
//                    phoneEntities.add(phon);
//                }
//            }
//            // phonesMap.get(personId);
//            PersonDTO personDTO = new PersonDTO();
//            personDTO.setPhones(phoneEntities);
//            personDTO.setName(per.getName());
//            personDTO.setId(per.getId());
//            personDTO.setAddress(per.getAddress());
//            personDTO.setEmail(per.getEmail());
//            all.add(personDTO);
//        }


        return all;
    }

    @Transactional
    @RequestMapping(value = "/persons/db/{id}", method = RequestMethod.POST)
    public PersonDTO editPerson(@RequestBody PersonDTO editedPerson, @PathVariable("id") Long id) {

        // update person
        Person person = personRepository.getOne(id);
        person.setName(editedPerson.getName());
        person.setAddress(editedPerson.getAddress());
        person.setEmail(editedPerson.getEmail());
        personRepository.save(person);

        // update phones
        List<Phone> phoneEntities = editedPerson.getPhones();
        phoneRepository.deleteAllByPersonId(id);
        for (Phone p : phoneEntities) {
            p.setPersonId(person.getId());
        }
        phoneRepository.saveAll(phoneEntities);

        // return DTO
        PersonDTO personDTO = new PersonDTO();
        personDTO.setEmail(person.getEmail());
        personDTO.setAddress(person.getAddress());
        personDTO.setName(person.getName());
        personDTO.setPhones(phoneEntities);

        return personDTO;
    }

    @Transactional
    @RequestMapping(value = "/persons/db/{id}", method = RequestMethod.DELETE)
    public void deletePerson(@PathVariable("id") Long id) {

        phoneRepository.deleteAllByPersonId(id);
        personRepository.deleteById(id);
//        List<Phone> phoneEntities = phoneRepository.findAll();
//        Iterator<Phone> iterator = phoneEntities.iterator();
//        while (iterator.hasNext()) {
//            if (iterator.next().getPersonId() != id) {
//                iterator.remove();
//            }
//        }


    }

    @RequestMapping(value = "/phones/db/{id}", method = RequestMethod.DELETE)
    public void deletePhone(@PathVariable("id") Long id) {

        phoneRepository.deleteById(id);

    }

    @RequestMapping(value = "/persons/db/add", method = RequestMethod.POST)
    public void addPerson(@RequestBody PersonDTO personDTO) {

        Person person = new Person();
        person.setEmail(personDTO.getEmail());
        person.setAddress(personDTO.getAddress());
        person.setName(personDTO.getName());
        personRepository.save(person);

        List<Phone> phoneEntities = personDTO.getPhones();

        for (Phone p : phoneEntities) {
            p.setPersonId(person.getId());
        }
        phoneRepository.saveAll(phoneEntities);


    }

}
