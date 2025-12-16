import axios from "axios";
import { API_URL } from "../config/api_config";

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev_booking_system.api.Util.JwtUtil;
import com.ev_booking_system.api.dto.EvDto;

import com.ev_booking_system.api.model.EvModel;
import com.ev_booking_system.api.repository.EvRepository;

@Service
public class EvService {
  export interface EV {
  id: string;
  make: string;
  model: string;
  year: number;
  batteryKwh: number;
  maxChargeKw: number;
  connectorTypes: string[];
  nickname: string;
  licensePlate: string;
}

export const loadEVs = async () => {
  const res = await axios.get(`${API_URL}/users/evs`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });

  return res.data.evs; // IMPORTANT
};

export const addEV = async (ev: Omit<EV, "id">): Promise<EV | null> => {
  try {
    const token = localStorage.getItem("token");

    const response = await axios.post<EV>(
      `${API_URL}/users/evs`,
      ev,
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );
      return response.data; // Axios automatically parses JSON
    } catch (err: any) {
      // Axios error handling
      if (err.response) {
        console.error("Server responded with error:", err.response.data);
      } else if (err.request) {
        console.error("No response received:", err.request);
      } else {
        console.error("Error setting up request:", err.message);
      }
      return null;
    }
  };  

    @Autowired
    private JwtUtil jwtUtil;

        public EvModel addEV(EvModel evModel, String token) {
        // Check if EV with this ID already exists
        /*if (evRepository.findById(evModel.getId()).isPresent()) {
            throw new RuntimeException("EV with this ID already exists");
        }*/

        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String userId = jwtUtil.extractUserId(token);
            evModel.setUserId(userId);

        } catch (Exception e) {
            e.printStackTrace(); // print exception for debugging
            return null; // or you can throw a custom exception
        }

        return evRepository.save(evModel);
    }
/* 
    public EvDto updateEv(EvDto evModel,String id){
        try {
        // Fetch user
        EvModel eModel = evRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Apply updates
        if (eModel.getMake() != null && !eModel.getMake().isEmpty()) {
            eModel.setMake(evModel.getMake());
        }
            eModel.setYear(evModel.getYear());

            eModel.setBatteryKwh(evModel.getBatteryKwh());

        if (eModel.getModel() != null && !eModel.getModel().isEmpty()) {
            eModel.setModel(evModel.getModel());
        }
            eModel.setMaxChargeKw(evModel.getMaxChargeKw());

        if (eModel.getConnectorTypes() != null && !eModel.getConnectorTypes().isEmpty()) {
            eModel.setConnectorTypes(evModel.getConnectorTypes());
        }
        if (eModel.getVin() != null && !eModel.getVin().isEmpty()) {
            eModel.setVin(evModel.getVin());
        }
        if (eModel.getLicensePlate() != null && !eModel.getLicensePlate().isEmpty()) {
            eModel.setLicensePlate(evModel.getLicensePlate());
        }
        if (eModel.getNickname() != null && !eModel.getNickname().isEmpty()) {
            eModel.setNickname(evModel.getNickname());
        }

        // Save user
        EvModel saved = evRepository.save(eModel);

        return EvMapper.toDto(saved);

    } catch (Exception e) {
        throw new RuntimeException("Profile update failed: " + e.getMessage());
        }
    }
*/
    public EvModel getEvById(String id){
        return evRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public boolean deleteUserEv(String token, String evId) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtUtil.extractUserId(token);

        Optional<EvModel> ev = evRepository.findById(evId);

        if (ev.isPresent() && ev.get().getUserId().equals(userId)) {
            evRepository.deleteById(evId);
            return true;
        }

    return false;
}

}
