package Utils;

import Bean.ConnectResponseBean;
import com.google.gson.Gson;

import java.util.ArrayList;

public class JsonCreater {
	static Gson gson = new Gson();
	public static String ConnectResponse(ArrayList<ConnectResponseBean.DataBaseList> items){
		ConnectResponseBean cBean = new ConnectResponseBean();
		cBean.DataBaseList = items;
		return gson.toJson(cBean);
	}
}
