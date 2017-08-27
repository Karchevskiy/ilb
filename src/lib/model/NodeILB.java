package lib.model;

import javafx.util.Pair;
import lib.errorHandling.ValueAlreadyExistsAndNotEqualsException;
import lib.errorHandling.ValueAlreadyExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ILBprocessing.configuration.SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED;

public class NodeILB implements Cloneable{

	public String primaryName="";
	public String ILBId="";
	/**
	 * Map of coordinates:
	 * 		key: catalogueID
	 *		value: List<Pair>: 1 entity: String - coordinate type
	 * 					       2 entity: Double - value
	 */
	private HashMap<String, List<javafx.util.Pair<String,Double>>> coordinates = new HashMap<>();
	/**
	 * Map of sources:
	 *		key: String catalogueID
	 * 		value: String source String
	 */
	private HashMap<String,String> mappedEntities = new HashMap<String,String>();
	/**
	 * Map of params:
	 * 		key: catalogueID
	 *		value: List<Pair>: 1 entity: String - param type
	 * 					       2 entity: String - value
	 */
	private HashMap<String,List<javafx.util.Pair<String,String>>> params = new HashMap<>();
	public NodeILB(){

	}
	public void addCoordinates(String catalogue, String coordType, Double value) throws ValueAlreadyExistsException{
		javafx.util.Pair<String,Double> entity = new Pair<>(coordType,value);

		if(coordinates.containsKey(catalogue)){
			if(coordinates.get(catalogue).contains(entity)){
				if(LOGGING_LEVEL_VERBOSE_ENABLED)throw new ValueAlreadyExistsException("cat: "+catalogue+" value: "+entity.toString());
			}else{
				for(Pair a:coordinates.get(catalogue)){
					if(a.getKey().equals(coordType)){
						if(a.getValue().equals(value)){
							throw new ValueAlreadyExistsException("cat: "+catalogue+" value: "+entity.toString());
						}else{
							throw new ValueAlreadyExistsAndNotEqualsException("cat: "+catalogue+" value: "+entity.toString()+" prevValue: "+a.getValue().toString());
						}
					}
				}
				coordinates.get(catalogue).add(entity);
			}
		}else{
			coordinates.put(catalogue,new ArrayList<>());
			coordinates.get(catalogue).add(entity);
		}
	}
	public List<javafx.util.Pair<String,Double>> getCoordinatesForCurrentCatalogue(String catalogue){
		return coordinates.get(catalogue);
	}
	public Double getCoordinatesForCatalogueByKey(String catalogue,String key){
		for(Pair a:coordinates.get(catalogue)){
			if(a.getKey().equals(key)){
				return (Double) a.getValue();
			}
		}
		return null;
	}
	public List<Double> getCoordinatesByKey(String key){
		ArrayList<Double> coordinatesList = new ArrayList<Double>();
		for(Map.Entry<String, List<Pair<String, Double>>> entity: coordinates.entrySet()){
			for(Pair<String, Double> pair: entity.getValue()){
				if(pair.getKey().equals(key)){
					coordinatesList.add(pair.getValue());
				}
			}
		}
		return coordinatesList;
	}

	public void addParams(String catalogue,String paramType, String value) throws ValueAlreadyExistsException{
		javafx.util.Pair<String,String> entity = new Pair<>(paramType,value);
		if(params.containsKey(catalogue)){
			if(params.get(catalogue).contains(entity)){
				//System.err.println("ValueAlreadyExistsWarning: cat: "+catalogue+" value: "+entity.toString());
			}else{
				for(Pair a:params.get(catalogue)){
					if(a.getKey().equals(paramType)){
						if(a.getValue().equals(value)){
							throw new ValueAlreadyExistsException("cat: "+catalogue+" value: "+entity.toString());
						}else{
							throw new ValueAlreadyExistsAndNotEqualsException("cat: "+catalogue+" value: "+entity.toString()+" prevValue: "+a.getValue().toString());
						}
					}
				}
				params.get(catalogue).add(entity);
			}
		}else{
				params.get(catalogue).add(entity);
		}
	}
	public List<javafx.util.Pair<String,String>> getParamsForCurrentCatalogue(String catalogue){
		return params.get(catalogue);
	}
	public ArrayList<String> getParamsForByKey(String key){
		ArrayList<String> result = new ArrayList<>();
		for(Map.Entry<String, List<Pair<String, String>>> cache: params.entrySet()){
			for(Pair<String, String> value:cache.getValue()){
				if(value.getKey().equals(key)){
					if(!result.contains(value.getValue())){
						result.add(value.getValue());
					}
				}
			}
		}
		return result;
	}

	public void addMappedEntity(String catalogue, String value) throws ValueAlreadyExistsException{
		if(mappedEntities.containsKey(catalogue)){
			if(mappedEntities.get(catalogue).equals(value)){
				throw new ValueAlreadyExistsException("cat: "+catalogue+" value: "+value);
			}else{
				throw new ValueAlreadyExistsAndNotEqualsException("cat: "+catalogue+" value: "+value+" prevValue: "+mappedEntities.get(catalogue));
			}
		}else {
			mappedEntities.put(catalogue, value);
			params.put(catalogue,new ArrayList<>());
		}
	}
	public List<String> getUsedCatalogues(){
		ArrayList<String> cataloguesList = new ArrayList<String>();
			for(String z:mappedEntities.keySet()){
				cataloguesList.add(z);
			}
		return cataloguesList;
	}
	public String getMappedEntityByCatalogue(String catalogue){
		return mappedEntities.get(catalogue);
	}

}