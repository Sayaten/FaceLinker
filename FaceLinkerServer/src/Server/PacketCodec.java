package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class PacketCodec {
	public static String readDelimiter(BufferedReader in) throws IOException{
		char charBuf[] = new char[1];
		String readMsg = "";
		short isdelim = 0;
		int size = 1, totalSize = 0;
		boolean isFirstDelimAppear = false;
		String strSize = "";
		
		// read character before packet delimiter
		while(in.read(charBuf, 0, 1) != -1){
			if(!isFirstDelimAppear){
				// read size of packet
				if(Packet.FIELD_DELIM.charAt(0) != charBuf[0]){
					strSize += charBuf[0];
				}
				else{
					totalSize = Integer.parseInt(strSize);
					if( totalSize >= 1){
						size = 1;
					}else{
						size = totalSize;
					}
					charBuf = new char[size];
					isFirstDelimAppear = true;
				}
			}
			// Packet.PK_DELIM == '?'
			else if(charBuf[0] == '?'){
				readMsg += charBuf[0];
				isdelim = 1;
				break;
			} else {
				readMsg += charBuf[0];
				totalSize -= size;
				continue;
			}
		}
		
		// remove '\n'
		while(in.read(charBuf, 0, 1) != -1)
		{
			if(charBuf[0] == '\n'){
				break;
			}
		}
		
		// if there isn't delimiter
		if(isdelim == 0 && charBuf[0]  != '\0'){
			System.out.println("MSG DELIM IS NOT FOUND!!");
		}
		return readMsg;
	}
	
	public static String addPacketSize(String src) throws IOException{
		int size = src.length();
		String addedSizePacket = Integer.toString(size) + Packet.FIELD_DELIM + src;
		return addedSizePacket;
	}
	
	public static Packet decodeHeader(String src) throws IOException{
		String type, data;
		int size;
		Scanner s = new Scanner(src).useDelimiter("\\"+Packet.FIELD_DELIM);
		
		type = s.next();
		s.skip(Packet.FIELD_DELIM);
		
		s.useDelimiter("\\"+Packet.PK_DELIM);
		data = s.next();
		
		return new Packet(type, data);
	}
	
	// About join request
	// Dncode join request packet data
	public static String encodeJoinReq(JoinReq pk_data){
		String data = Packet.PK_JOIN_REQ + Packet.FIELD_DELIM 
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ pk_data.getPassword() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
				
		return data;
	}
	// Decode join request packet data
	public static JoinReq decodeJoinReq(String pk_data) throws IOException{
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		JoinReq dst = new JoinReq();
		
		dst.setScreen_name(s.next());
		dst.setPassword(s.next());
		
		return dst;
	}
	
	// About join ack
	// Decode join response packet data
	public static String encodeJoinAck(JoinAck pk_data){
		String data = Packet.PK_JOIN_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		
		return data;
	}

	// Decode join response packet data
	public static JoinAck decodeJoinAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		JoinAck dst = new JoinAck();
		
		dst.setResult(s.nextInt());
		
		return dst;
	}
	
	// About login request
	// Encode login request
	public static String encodeLoginReq(LoginReq pk_data){
		String data = Packet.PK_LOGIN_REQ + Packet.FIELD_DELIM
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ pk_data.getPassword() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		
		return data;
	}
	// Decode Login response packet data
	public static LoginReq decodeLoginReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LoginReq dst = new LoginReq();
	
		dst.setScreen_name(s.next());
		dst.setPassword(s.next());
		
		return dst;
	}
	
	// About login ack
	// Decode login response packet data
	public static String encodeLoginAck(LoginAck pk_data){
		String data = Packet.PK_LOGIN_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM;
		
		if (pk_data.getResult() == Packet.SUCCESS){
			data = data + pk_data.getName() + Packet.FIELD_DELIM
				+ pk_data.getJob() + Packet.FIELD_DELIM
				+ pk_data.getGender() + Packet.FIELD_DELIM
				+ pk_data.getCountry() + Packet.FIELD_DELIM
				+ pk_data.getProfile_img() + Packet.FIELD_DELIM;
		}
		
		data += Packet.PK_DELIM;	
		
		return data;
	}

	// Decode login response packet data
	public static LoginAck decodeLoginAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		LoginAck dst = new LoginAck();
		
		dst.setResult(s.nextInt());
		if(dst.getResult() == Packet.SUCCESS)
		{
			dst.setName(s.next());
			dst.setJob(s.next());
			dst.setGender(s.next());
			dst.setCountry(s.next());
			dst.setProfile_img(s.next());
		}
		return dst;
	}
	
	// About profile write request
	// Encode profile write request packet data
	public static String encodeProfileWriteReq(ProfileModifyReq pk_data){
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
	public static ProfileModifyReq decodeProfileWriteReq(String pk_data){
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
	public static String encodeProfileWriteAck(ProfileModifyAck pk_data){
		String data = Packet.PK_PRO_WRITE_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Decode profile write response packet data
	public static ProfileModifyAck decodeProfileWriteAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileModifyAck dst = new ProfileModifyAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}	
	
	// About part register Req
	// Encode part register request packet
	public static String encodePartRegisterReq(PartRegisterReq pk_data){
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
	public static PartRegisterReq decodePartRegisterReq(String pk_data){
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
	public static String encodePartRegisterAck(PartRegisterAck pk_data){
		String data = Packet.PK_PART_REG_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Decode part register request packet data
	public static ProfileModifyAck decodePartRegisterAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileModifyAck dst = new ProfileModifyAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}
	
	// About part get request
	// Encode part get request packet
	public static String encodePartGetReq(PartGetReq pk_data) {
		String data = Packet.PK_PART_GET_REQ + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getUser_id()) + Packet.FIELD_DELIM
				+ pk_data.getPart_type() + Packet.FIELD_DELIM 
				+ Packet.PK_DELIM;
		return data;
	}

	// Decode part get request packet data
	public static PartGetReq decodePartGetReq(String pk_data) {
		Scanner s = new Scanner(pk_data).useDelimiter("\\" + Packet.FIELD_DELIM);
		PartGetReq dst = new PartGetReq();

		dst.setUser_id(s.nextInt());
		dst.setPart_type(s.next());

		return dst;
	}
	
	// About part get ack
	// Encode part get response packet data
	public static String encodePartGetAck(PartGetAck pk_data){
		String data = Packet.PK_PART_GET_ACK + Packet.FIELD_DELIM
				+ pk_data.getPart() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Decode part get response packet data
	public static PartGetAck decodePartGetAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		PartGetAck dst = new PartGetAck();
		
		dst.setPart(s.next());

		return dst;
	}	
	
	// About ideal type register Req
	// Encode ideal type request packet
	public static String encodeIdealTypeRegisterReq(IdealTypeRegisterReq pk_data){
		String data = Packet.PK_IDEAL_REG_REQ + Packet.FIELD_DELIM
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ pk_data.getIdeal_type() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}

	// Decode ideal type register request packet data
	public static IdealTypeRegisterReq decodeIdealTypeRegisterReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		IdealTypeRegisterReq dst = new IdealTypeRegisterReq();
		
		dst.setScreen_name(s.next());
		dst.setIdeal_type(s.next());
		
		return dst;
	}
	
	// About ideal type register ack
	// Encode ideal type register response packet data
	public static String encodeIdealTypeRegisterAck(IdealTypeRegisterAck pk_data){
		String data = Packet.PK_IDEAL_REG_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Encode ideal type register response packet data
	public static IdealTypeRegisterAck decodeIdealTypeRegisterAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		IdealTypeRegisterAck dst = new IdealTypeRegisterAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}	

	// About ideal type search Req
	// Encode ideal type search request packet
	public static String encodeIdealTypeSearchReq(IdealTypeSearchReq pk_data){
		String data = Packet.PK_IDEAL_SCH_REQ + Packet.FIELD_DELIM
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}

	// Decode ideal type search request packet data
	public static IdealTypeSearchReq decodeIdealTypeSearchReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		IdealTypeSearchReq dst = new IdealTypeSearchReq();
		
		dst.setScreen_name(s.next());
		
		return dst;
	}
	
	// About ideal type search ack
	// Encode ideal type search response packet data
	public static String encodeIdealTypeSearchAck(IdealTypeSearchAck pk_data){
		String data = Packet.PK_IDEAL_SCH_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getIdeal_types().size()) + Packet.FIELD_DELIM;
		for(ImageNameSet ideal_type : pk_data.getIdeal_types()){
			data += (ideal_type.getScreen_name() + Packet.FIELD_DELIM
					+ ideal_type.getImage() + Packet.FIELD_DELIM);
		}
		data += Packet.PK_DELIM;
		return data;
	}
	
	// Encode ideal type search response packet data
	public static IdealTypeSearchAck decodeIdealTypeSearchAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		IdealTypeSearchAck dst = new IdealTypeSearchAck();
		
		int size = Integer.parseInt(s.next());
		
		for(int i = 0 ; i < size ; ++i){
			dst.getIdeal_types().add(new ImageNameSet(s.next(), s.next()));
		}

		return dst;
	}
	
	// About profile modify Req
	// Encode profile modify request packet
	public static String encodeProfileModifyReq(ProfileModifyReq pk_data){
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
	public static ProfileModifyReq decodeProfileModifyReq(String pk_data){
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
	public static String encodeProfileModifyAck(ProfileModifyAck pk_data){
		String data = Packet.PK_PRO_MODIFY_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Encode profile modify response packet data
	public static ProfileModifyAck decodeProfileModifyAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileModifyAck dst = new ProfileModifyAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}	
	
	/*
	 * Codec Contact
	 */
	
	// About Contact req
	// Encode contact request packet
	public static String encodeContactReq(ContactReq pk_data){
		String data = Packet.PK_CONTACT_REQ + Packet.FIELD_DELIM
				+ pk_data.getSend_user() + Packet.FIELD_DELIM
				+ pk_data.getRec_user() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	// Decode contact request packet
	public static ContactReq decodeContactReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ContactReq dst =  new ContactReq();
		
		dst.setSend_user(s.next());
		dst.setRec_user(s.next());
		
		return dst;
	}
	
	// About Contact ack
	// Encode contact ack packet
	public static String encodeContactAck(ContactAck pk_data){
		String data = Packet.PK_CONTACT_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	// Decode contact ack packet
	public static ContactAck decodeContactAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ContactAck dst = new ContactAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}
	
	/*
	 * Codec Reply Contact
	 */
	
	// About Reply Contact req
	// Encode reply contact request packet
	public static String encodeReplyContactReq(ReplyContactReq pk_data){
		String data = Packet.PK_REPLY_CON_REQ + Packet.FIELD_DELIM
				+ pk_data.getSend_user() + Packet.FIELD_DELIM
				+ pk_data.getRec_user() + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getReply()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	// Decode reply contact request packet
	public static ReplyContactReq decodeReplyContactReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ReplyContactReq dst =  new ReplyContactReq();
		
		dst.setSend_user(s.next());
		dst.setRec_user(s.next());
		dst.setReply(s.nextInt());
		return dst;
	}
	
	// About Reply Contact ack
	// Encode reply contact ack packet
	public static String encodeReplyContactAck(ReplyContactAck pk_data){
		String data = Packet.PK_REPLY_CON_ACK + Packet.FIELD_DELIM
				+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	// Decode reply contact ack packet
	public static ReplyContactAck decodeReplyContactAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ReplyContactAck dst = new ReplyContactAck();
		
		dst.setResult(s.nextInt());

		return dst;
	}
	
	/*
	 * Codec Get Contact
	 */
	
	// About Get Contact req
	// Encode get contact request packet
	public static String encodeGetContactReq(GetContactReq pk_data){
		String data = Packet.PK_GET_CON_REQ + Packet.FIELD_DELIM
				+ pk_data.getScreen_name() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	// Decode get contact request packet
	public static GetContactReq decodeGetContactReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		GetContactReq dst =  new GetContactReq();
		
		dst.setScreen_name(s.next());
		return dst;
	}
	
	// About Get Contact ack
	// Encode get contact ack packet
	public static String encodeGetContactAck(GetContactAck pk_data){
		int size = pk_data.getContacts().size();
		String data = Packet.PK_GET_CON_ACK + Packet.FIELD_DELIM
					+ Integer.toString(size) + Packet.FIELD_DELIM;
		for(ContactInfo contact : pk_data.getContacts()){
			data += ( contact.getContact().getScreen_name() + Packet.FIELD_DELIM
					+ contact.getContact().getImage() + Packet.FIELD_DELIM
					+ Integer.toString(contact.getIsAccept()) + Packet.FIELD_DELIM );
		}
		data += Packet.PK_DELIM;
		return data;
	}
	// Decode get contact ack packet
	public static GetContactAck decodeGetContactAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		GetContactAck dst = new GetContactAck();
		int size = s.nextInt();
		
		for(int i = 0 ; i < size ; ++i){
			dst.getContacts().add(new ContactInfo( new ImageNameSet(s.next(), s.next()), s.nextInt() ));
		}
		
		return dst;
	}
	
	// About End of Connection
	// Encode End of Connection Req
	public static String encodeConnectionEndReq(ConnectionEndReq pk_data){
		String data = Packet.PK_CONNECTION_END + Packet.FIELD_DELIM
				+ pk_data.getIsEnd() + Packet.FIELD_DELIM
				+ Packet.PK_DELIM;
		return data;
	}
	
	// Decode End of Connection Req
	public static ConnectionEndReq decodeConnectionEndReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ConnectionEndReq dst = new ConnectionEndReq();
		
		dst.setIsEnd(s.next());

		return dst;
	}
	
	// About Profile Get
	// Encode Profile Get Req
	public static String encodeProfileGetReq(ProfileGetReq pk_data){
		String data = Packet.PK_PRO_GET_REQ + Packet.FIELD_DELIM
					+ pk_data.getScreen_name() + Packet.FIELD_DELIM
					+ Packet.PK_DELIM;
		return data;
	}
	// Decode Profile Get Req
	public static ProfileGetReq decodeProfileGetReq(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileGetReq dst = new ProfileGetReq();
		
		dst.setScreen_name(s.next());

		return dst;
	}
	
	// Encode Profile Get Ack
	public static String encodeProfileGetAck(ProfileGetAck pk_data){
		String data = Packet.PK_PRO_GET_REQ + Packet.FIELD_DELIM
					+ Integer.toString(pk_data.getResult()) + Packet.FIELD_DELIM
					+ pk_data.getName() + Packet.FIELD_DELIM
					+ pk_data.getGender() + Packet.FIELD_DELIM
					+ pk_data.getJob() + Packet.FIELD_DELIM
					+ pk_data.getCountry() + Packet.FIELD_DELIM
					+ pk_data.getProfile_img() + Packet.FIELD_DELIM
					+ Packet.PK_DELIM;
		return data;
	}
	
	// Decode Profile Get Ack
	public static ProfileGetAck decodeProfileGetAck(String pk_data){
		Scanner s = new Scanner(pk_data).useDelimiter("\\"+Packet.FIELD_DELIM);
		ProfileGetAck dst = new ProfileGetAck();
		
		dst.setResult(s.nextInt());
		dst.setName(s.next());
		dst.setGender(s.next());
		dst.setJob(s.next());
		dst.setCountry(s.next());
		dst.setProfile_img(s.next());
		
		return dst;
	}
}
