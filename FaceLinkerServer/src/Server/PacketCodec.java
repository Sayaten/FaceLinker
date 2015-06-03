package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class PacketCodec {
	public static String read_delim(BufferedReader in) throws IOException{
		char charBuf[] = new char[1];
		String readMsg = "";
		short isdelim = 0;
		
		// read character before read delimiter
		while(in.read(charBuf, 0, 1) != -1){
			// Packet.PK_DELIM == '\n'
			if(charBuf[0] == '?'){
				readMsg += charBuf[0];
				isdelim = 1;
				break;
			} else {
				readMsg += charBuf[0];
				continue;
			}
		}
		
		// if there isn't delimiter
		if(isdelim == 0 && charBuf[0]  != '\0'){
			System.out.println("MSG DELIM IS NOT FOUND!!");
		}
		return readMsg;
	}
	
	public static Packet decode_Header(String src) throws IOException{
		String type, data;
		Scanner s = new Scanner(src).useDelimiter("\\"+Packet.FIELD_DELIM);
		
		type = s.next();
		s.skip(Packet.FIELD_DELIM);
		
		s.useDelimiter("\\"+Packet.PK_DELIM);
		data = s.next();
		
		return new Packet(type, data);
	}
	
	// About join request
	// Dncode join request packet data
	public static String encode_JoinReq(JoinReq pk_data){
		String data = Packet.PK_JOIN_REQ + Packet.FIELD_DELIM 
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ pk_data.getPassword() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
				
		return data;
	}
	// Decode join request packet data
	public static JoinReq decode_JoinReq(String pk_data) throws IOException{
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		JoinReq dst = new JoinReq();
		
		dst.setScreen_name(s.next());
		dst.setPassword(s.next());
		
		return dst;
	}
	
	// About join ack
	// Decode join response packet data
	public static String encode_JoinAck(JoinAck pk_data){
		String data = Packet.PK_JOIN_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		
		return data;
	}

	// Decode join response packet data
	public static JoinAck decode_JoinAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		JoinAck dst = new JoinAck();
		
		dst.setResult(s.nextInt());
		
		return dst;
	}
	
	// About profile write request
	// Encode profile write request packet data
	public static String encode_ProfileWriteReq(ProfileModifyReq pk_data){
		String data = Packet.PK_PRO_WRITE_REQ + Packet.FIELD_DELIM
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ pk_data.getName() + Packet.FIELD_DELIM
				+ pk_data.getGender() + Packet.FIELD_DELIM
				+ pk_data.getJob() + Packet.FIELD_DELIM
				+ pk_data.getCountry() + Packet.FIELD_DELIM
				+ pk_data.getProfile_img() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}

	// Decode profile write request packet data
	public static ProfileModifyReq decode_ProfileWriteReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileModifyReq dst = new ProfileModifyReq();
		
		dst.setScreen_name(s.next());
		dst.setName(s.next());
		dst.setGender(s.next());
		dst.setJob(s.next());
		dst.setCountry(s.next());
		dst.setProfile_img(s.next());
		
		return dst;
	}
	
	// About profile write ack
	// Encode profile write response packet data
	public static String encode_ProfileWriteAck(ProfileModifyAck pk_data){
		String data = Packet.PK_PRO_WRITE_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Decode profile write response packet data
	public static ProfileModifyAck decode_ProfileWriteAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileModifyAck dst = new ProfileModifyAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}	
	
	// About part register Req
	// Encode part register request packet
	public static String encode_PartRegisterReq(PartRegisterReq pk_data){
		String data = Packet.PK_PART_REG_REQ + Packet.FIELD_DELIM
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ pk_data.getEyes() + Packet.FIELD_DELIM
				+ pk_data.getNose() + Packet.FIELD_DELIM
				+ pk_data.getMouth() + Packet.FIELD_DELIM
				+ pk_data.getFace() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}

	// Decode part register request packet data
	public static PartRegisterReq decode_PartRegisterReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		PartRegisterReq dst = new PartRegisterReq();
		
		dst.setScreen_name(s.next());
		dst.setEyes(s.next());
		dst.setNose(s.next());
		dst.setMouth(s.next());
		dst.setFace(s.next());
		
		return dst;
	}
	
	// About part register ack
	// Encode part register response packet data
	public static String encode_PartRegisterAck(PartRegisterAck pk_data){
		String data = Packet.PK_PART_REG_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Decode part register request packet data
	public static ProfileModifyAck decode_PartRegisterAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileModifyAck dst = new ProfileModifyAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}
	
	// About part get request
	// Encode part get request packet
	public static String encode_PartGetReq(PartGetReq pk_data) {
		String data = Packet.PK_PART_REG_REQ + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getUser_id()) + Packet.FIELD_DELIM
				+ pk_data.getPart_type() + Packet.FIELD_DELIM 
				+ Packet.PK_DELIM;
		return data;
	}

	// Decode part get request packet data
	public static PartGetReq decode_PartGetReq(String pk_data) {
		Scanner s = new Scanner(pk_data).useDelimiter("\\" + Packet.FIELD_DELIM);
		PartGetReq dst = new PartGetReq();

		dst.setUser_id(s.nextInt());
		dst.setPart_type(s.next());

		return dst;
	}
	
	// About part get ack
	// Encode part get response packet data
	public static String encode_PartGetAck(PartGetAck pk_data){
		String data = Packet.PK_PART_GET_ACK + Packet.FIELD_DELIM
				+ pk_data.getPart() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Decode part get response packet data
	public static PartGetAck decode_PartGetAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		PartGetAck dst = new PartGetAck();
		
		dst.setPart(s.next());

		return dst;
	}	
	
	// About ideal type register Req
	// Encode ideal type request packet
	public static String encode_IdealTypeRegisterReq(IdealTypeRegisterReq pk_data){
		String data = Packet.PK_IDEAL_REG_REQ + Packet.FIELD_DELIM
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ pk_data.getIdeal_type() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}

	// Decode ideal type register request packet data
	public static IdealTypeRegisterReq decode_IdealTypeRegisterReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		IdealTypeRegisterReq dst = new IdealTypeRegisterReq();
		
		dst.setScreen_name(s.next());
		dst.setIdeal_type(s.next());
		
		return dst;
	}
	
	// About ideal type register ack
	// Encode ideal type register response packet data
	public static String encode_IdealTypeRegisterAck(IdealTypeRegisterAck pk_data){
		String data = Packet.PK_IDEAL_REG_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Encode ideal type register response packet data
	public static IdealTypeRegisterAck decode_IdealTypeRegisterAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		IdealTypeRegisterAck dst = new IdealTypeRegisterAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}	

	// About ideal type search Req
	// Encode ideal type search request packet
	public static String encode_IdealTypeSearchReq(IdealTypeSearchReq pk_data){
		String data = Packet.PK_IDEAL_SCH_REQ + Packet.FIELD_DELIM
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}

	// Decode ideal type search request packet data
	public static IdealTypeSearchReq decode_IdealTypeSearchReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		IdealTypeSearchReq dst = new IdealTypeSearchReq();
		
		dst.setScreen_name(s.next());
		
		return dst;
	}
	
	// About ideal type search ack
	// Encode ideal type search response packet data
	public static String encode_IdealTypeSearchAck(IdealTypeSearchAck pk_data){
		String data = Packet.PK_IDEAL_SCH_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getIdeal_types().size()) + Packet.FIELD_DELIM;
		for(IdealType ideal_type : pk_data.getIdeal_types()){
			data = ideal_type.getScreen_name() + Packet.FIELD_DELIM
					+ ideal_type.getImage() + Packet.FIELD_DELIM;
		}
		data += Packet.PK_DELIM;
		return data;
	}
	
	// Encode ideal type search response packet data
	public static IdealTypeSearchAck decode_IdealTypeSearchAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		IdealTypeSearchAck dst = new IdealTypeSearchAck();
		
		int size = Integer.parseInt(s.next());
		for(int i = 0 ; i < size ; ++i){
			dst.getIdeal_types().add(new IdealType(s.next(), s.next()));
		}

		return dst;
	}
	
	// About profile modify Req
	// Encode profile modify request packet
	public static String encode_ProfileModifyReq(ProfileModifyReq pk_data){
		String data = Packet.PK_PRO_MODIFY_REQ + Packet.FIELD_DELIM
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ ((pk_data.getName() != null)		  ? pk_data.getName() : " ")		+ Packet.FIELD_DELIM
				+ ((pk_data.getGender() != null)	  ? pk_data.getGender() : " ") 		+ Packet.FIELD_DELIM
				+ ((pk_data.getJob() != null)		  ? pk_data.getJob() : " ")			+ Packet.FIELD_DELIM
				+ ((pk_data.getCountry() != null)	  ? pk_data.getCountry() : " ") 	+ Packet.FIELD_DELIM
				+ ((pk_data.getProfile_img() != null) ? pk_data.getProfile_img() : " ") + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}

	// Decode profile modify request packet data
	public static ProfileModifyReq decode_ProfileModifyReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileModifyReq dst = new ProfileModifyReq();
		String temp;
		
		dst.setScreen_name(s.next());

		temp = s.next();
		if(temp.compareTo(" ") != 0) dst.setName(temp);
		temp = s.next();
		if(temp.compareTo(" ") != 0) dst.setGender(temp);
		temp = s.next();
		if(temp.compareTo(" ") != 0) dst.setJob(temp);
		temp = s.next();
		if(temp.compareTo(" ") != 0) dst.setCountry(temp);
		temp = s.next();
		if(temp.compareTo(" ") != 0) dst.setProfile_img(temp);
		
		return dst;
	}
	
	// About profile modify ack
	// Encode profile modify register response packet data
	public static String encode_ProfileModifyAck(ProfileModifyAck pk_data){
		String data = Packet.PK_PRO_MODIFY_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Encode profile modify response packet data
	public static ProfileModifyAck ProfileModifyAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileModifyAck dst = new ProfileModifyAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}	
}
