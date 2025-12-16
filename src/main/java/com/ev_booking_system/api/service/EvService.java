package com.ev_booking_system.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;

@Service
public class EVService {

    @Autowired
    private EvRepository evRepository;

    public EvModel addEv(EvModel ev) {
        return evRepository.save(ev);
    }
}
