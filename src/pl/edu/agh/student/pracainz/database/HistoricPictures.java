package pl.edu.agh.student.pracainz.database;

public class HistoricPictures {
	
	private String latitude;
	private String langitude;
	private int id;
	private String path;
	private String description;
	
	
	public HistoricPictures(){		
	}
	
	public HistoricPictures(String latitude, String langitude, String path, int id, String description){
		this.description = description;
		this.latitude = latitude;
		this.langitude = langitude;
		this.path = path;
		this.id = id;
	}
	
	public HistoricPictures(String latitude, String langitude, String path, String description){		
		this.latitude = latitude;
		this.langitude = langitude;
		this.path = path;		
		this.description = description;
	}
	
	
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatittude(String latitude) {
		this.latitude = latitude;
	}
	public String getLangitude() {
		return langitude;
	}
	public void setLangitude(String langitude) {
		this.langitude = langitude;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
