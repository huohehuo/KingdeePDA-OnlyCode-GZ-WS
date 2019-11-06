package Utils;

import javax.servlet.http.HttpServletRequest;

public class getDataBaseUrl {
	public static String getUrl(String ip ,String port,String database){
		return "jdbc:sqlserver://"+ip+":"+port+";DatabaseName="+database;
	}
	public static String getUrl(HttpServletRequest request){
		return "jdbc:sqlserver://"+request.getParameter("sqlip")+":"+request.getParameter("sqlport")+";DatabaseName="+request.getParameter("sqlname");
	}
}
