package Server;

public class IdealType {
	private String screen_name;
	private String image;
	
	public IdealType(){
		
	}
	public IdealType(String screen_name, String image){
		this.screen_name = screen_name;
		this.image = image;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
