package Messages;

import java.util.Map;

import com.google.gson.Gson;

public class TestGson {

	public static void main(String[] args) {
		
		Gson gson = new Gson();
		
		Query q = new Query("file.txt",5,new Msg(12,123),1024);
		
		String query = gson.toJson(q);
		
		System.out.println(gson.toJson(q));
		
		Message m = new Message("query",query);
		
		Message m2 = gson.fromJson(gson.toJson(m), Message.class);
		
		System.out.println(gson.toJson(m2));
		
		Message m3 = gson.fromJson(gson.toJson(m2), Message.class);
		
		//System.out.println(m3.getMsgType());
		
		if (m3.getMsgType().equals("query")) {
			//System.out.println(m3.getMessage());
			Query a = gson.fromJson(m3.getMessage(), Query.class);
			System.out.println(gson.toJson(a));
		}
		
		String file = "test.txt";
        String obtain = "{\"msgType\":\"Obtain\",\"message\":\"" + file + "\"}";
        Message o = gson.fromJson(obtain, Message.class);
        System.out.println(o.getMsgType());
		
		
	}

}
