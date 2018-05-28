package storm.blueprints;

public class DiagnosisEvent {

	private double lat;
	private double lng;
	private long time;
	private String diagnosisCode;

	public DiagnosisEvent(double lat, double lng, long time, String diag) {
		this.setLat(lat);
		this.setLng(lng);
		this.setTime(time);
		this.setDiagnosisCode(diag);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getDiagnosisCode() {
		return diagnosisCode;
	}

	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

}
