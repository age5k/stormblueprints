package storm.blueprints;

public class DiagnosisEvent {

	public double lat;
	public double lng;
	public long time;
	public String diagnosisCode;

	public DiagnosisEvent(double lat, double lng, long time, String diag) {
		this.lat = (lat);
		this.lng = (lng);
		this.time = (time);
		this.diagnosisCode = (diag);
	}

}
