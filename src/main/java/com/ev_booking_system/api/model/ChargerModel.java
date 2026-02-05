package com.ev_booking_system.api.model;

import lombok.Data;

@Data
public class ChargerModel {

    private String id;
    private String stationId;
    private String connectorType;
    private double maxPowerKw;
    private ChargerStatus status;
    private String ocppEndpointId;
    private String qrCode;
    private String name;
    private Integer portNumber;

    public enum ChargerStatus {
        AVAILABLE,
        OCCUPIED,
        OUT_OF_SERVICE,
        RESERVED,
        CHARGING;

        public static ChargerStatus fromString(String value) {
            if (value == null) return AVAILABLE;
            switch (value.toLowerCase()) {
                case "available":
                    return AVAILABLE;
                case "occupied":
                    return OCCUPIED;
                case "out-of-service":
                case "outofservice":
                    return OUT_OF_SERVICE;
                case "reserved":
                    return RESERVED;
                case "charging":
                    return CHARGING;
                default:
                    return AVAILABLE;
            }
        }

        public String toJsonString() {
            switch (this) {
                case AVAILABLE:
                    return "available";
                case OCCUPIED:
                    return "occupied";
                case OUT_OF_SERVICE:
                    return "out-of-service";
                case RESERVED:
                    return "reserved";
                case CHARGING:
                    return "charging";
                default:
                    return "available";
            }
        }
    }

    public String getDisplayName() {
        if (name != null) return name;
        if (portNumber != null) return "Port " + portNumber;
        return "Port " + id.substring(0, 4);
    }

    public String getPowerDisplay() {
        return ((int) maxPowerKw) + " kW";
    }

    public boolean isAvailable() {
        return status == ChargerStatus.AVAILABLE;
    }
}
