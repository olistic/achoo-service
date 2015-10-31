package uy.achoo.util;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import uy.achoo.customModel.PharmacyJPA;

import java.util.Collections;
import java.util.List;

/**
 * @author Alfredo El Ters
 * @author Diego Muracciole
 * @author Mathías Cabano
 * @author Matías Olivera
 */
public class GoogleService {
    private static final String API_KEY = "AIzaSyDAUcHoW1TVzytyQFu2cxULgdgHIvixlJU";

    public static final List<PharmacyJPA> orderPharmaciesByLocation(Double latitude, Double longitude, List<PharmacyJPA> pharmacyList) throws Exception {
        GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
        String[] destinations = new String[pharmacyList.size()];
        for (int i = 0; i < pharmacyList.size(); i++) {
            destinations[i] = pharmacyList.get(i).getAddress();
        }
        DistanceMatrixApiRequest distanceMatrixApi = new DistanceMatrixApiRequest(context);

        distanceMatrixApi.origins(new LatLng(latitude,longitude));
        distanceMatrixApi.destinations(destinations);
        DistanceMatrix apiRequestResult = distanceMatrixApi.await();
        DistanceMatrixRow matrixRow = apiRequestResult.rows[apiRequestResult.rows.length-1];;
        DistanceMatrixElement matrixElement;
        for (int j = 0; j < matrixRow.elements.length; j++) {
            matrixElement = matrixRow.elements[j];
            pharmacyList.get(j).setDistanceFromOrigin(matrixElement.distance.inMeters);
        }
        Collections.sort(pharmacyList, (o1, o2) -> o1.getDistanceFromOrigin().compareTo(o2.getDistanceFromOrigin()));
        return pharmacyList;
    }
}
