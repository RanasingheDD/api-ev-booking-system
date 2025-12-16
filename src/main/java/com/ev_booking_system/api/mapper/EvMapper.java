package com.ev_booking_system.api.mapper;

import com.ev_booking_system.api.model.EvModel;

import org.springframework.stereotype.Component;

import com.ev_booking_system.api.dto.EvDto;

import java.time.LocalDateTime;

@Component
public class EvMapper {

    // Converts EvModel to EvDto
    public static EvDto toDto(EvModel ev) {
        EvDto dto = new EvDto();
        dto.setId(ev.getId());
        dto.setMake(ev.getMake());
        dto.setModel(ev.getModel());
        dto.setYear(ev.getYear());
        dto.setBatteryKwh(ev.getBatteryKwh());
        dto.setMaxChargeKw(ev.getMaxChargeKw());
        dto.setConnectorTypes(ev.getConnectorTypes());
        dto.setVin(ev.getVin());
        dto.setLicensePlate(ev.getLicensePlate());
        dto.setNickname(ev.getNickname());
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
}
