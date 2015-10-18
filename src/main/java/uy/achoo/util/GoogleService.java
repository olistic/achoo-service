package uy.achoo.util;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import uy.achoo.customModel.DrugstoreJPA;

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

    public static final List<DrugstoreJPA> orderDrugstoresByLocation(Double latitude, Double longitude, List<DrugstoreJPA> drugstoreList) throws Exception {
        GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
        String[] destinations = new String[drugstoreList.size()];
        for (int i = 0; i < drugstoreList.size(); i++) {
            destinations[i] = drugstoreList.get(i).getAddress();
        }
        DistanceMatrixApiRequest distanceMatrixApi = new DistanceMatrixApiRequest(context);

        distanceMatrixApi.origins(new LatLng(latitude,longitude));
        distanceMatrixApi.destinations(destinations);
        DistanceMatrix apiRequestResult = distanceMatrixApi.await();
        DistanceMatrixRow matrixRow = apiRequestResult.rows[apiRequestResult.rows.length-1];;
        DistanceMatrixElement matrixElement;
        for (int j = 0; j < matrixRow.elements.length; j++) {
            matrixElement = matrixRow.elements[j];
            drugstoreList.get(j).setDistanceFromOrigin(matrixElement.distance.inMeters);
        }
        Collections.sort(drugstoreList, (o1, o2) -> o1.getDistanceFromOrigin().compareTo(o2.getDistanceFromOrigin()));
        return drugstoreList;
    }
}
