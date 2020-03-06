package WebIO;

import Utils.JDBCUtil;
import Utils.Lg;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static Utils.JDBCUtil.ReadAsChars;

/**
 * 销售出库分类汇总
 */
@WebServlet(urlPatterns = "/SoldOutSortCheckIO")
public class SoldOutSortCheckIO extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String parameter=null;
        try{
            parameter = ReadAsChars(request);//解密数据
        }catch (Exception e){
            response.getWriter().write(gson.toJson(new WebResponse(false,"上传失败,请求体解析错误")));
        }
//        String barcode = request.getParameter("number");
//        String fid = request.getParameter("fid");
//        String userid = request.getParameter("name");
//        String imie = request.getParameter("storage_name");
//        String qty="0";
        Connection conn = null;
        PreparedStatement sta = null;
        PreparedStatement sta2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String backString = "";
        String condition = "";


        PostBean pBean = gson.fromJson(parameter, PostBean.class);
        if (null ==pBean.FLevel ||"".equals(pBean.FLevel)){
            response.getWriter().write(gson.toJson(new WebResponse(false,"查询条件(等级)为空，查询失败")));
            return;
        }

        if ("1".equals(pBean.FLevel)){
            condition +=" and t1.FMDUserID ='"+pBean.fid+"'";
        }

        if ("2".equals(pBean.FLevel)){
            condition +=" and t2.FSupplyID in（select FCustID from t_UserPDASupply where FID ='"+pBean.fid+"')";
            //门店
            if (null !=pBean.md_ids &&!"".equals(pBean.md_ids)){
                condition += " and t1.FMDUserID in(" + pBean.md_ids+")";
            }

        }
        if ("3".equals(pBean.FLevel)){
            //客户
            if (null !=pBean.client_ids &&!"".equals(pBean.client_ids)){
                condition += " and t2.FSupplyID in(" + pBean.client_ids+")";
            }
            //门店
            if (null !=pBean.md_ids &&!"".equals(pBean.md_ids)){
                condition += " and t1.FMDUserID in(" + pBean.md_ids+")";
            }

        }

        ArrayList<WebResponse.SoldOutSortBean> container = new ArrayList<>();
//        Lg.e("列表",testBS);
            try {
                conn = JDBCUtil.getConn4Web();
//                String SQL = "select t2.FModel,t2.FNumber as 物料编码,t2.FName as 物料名称,convert(float,t1.FQty) as 基本单位库存,convert(float,t1.FSecQty) as 辅助单位库存,t6.FName as 辅助单位,t5.FName as 基本单位,t3.FName as 仓库,t4.FName as 仓位,t1.FBatchNo as 批次,t1.FKFDate as 生产日期,t1.FKFPeriod as 保质期,t1.FStockID,t1.FItemID from ICInventory t1 left join t_ICItem t2 on t1.FItemID = t2.FItemID left join t_stock t3 on t1.FStockID = t3.FItemID left join t_stockPlace t4 on t1.FStockPlaceID = t4.FSPID left join t_Measureunit t5 on t2.FUnitID = t5.FItemID left join t_Measureunit t6 on t2.FSecUnitID = t6.FItemID  where 1=1 and t1.FQty<>0 and t1.FStockID in(select FStockID from t_UserPDASupplyStock where FID = "+fid+" ) "+condition;
                String SQL = "select t.产品系列,t.数量,t2.FQty as 当前库存,t.单价 as 金额  from ( select t_2043.FName as 产品系列,isnull(t_2043.FItemID,0) as FCPXLID,sum(t4.FQty) as 数量,sum(CONVERT(decimal(28,4),ISNULL( t1.FPrice,0))) as 单价  from t_PDABarCodeSign_Out t1 inner join ICStockBill t2 on t1.FInterID = t2.FInterID inner join t_Organization t3 on t2.FSupplyID = t3.FItemID inner join t_PDABarCodeSign t4 on t1.FBarCode=t4.FBarCode inner join t_ICItem t5 on t5.FItemID = t4.FItemID left join t_Item_2043  t_2043  on t5.F_109=t_2043.FItemID   where t1.FTypeID=21 "+condition +" group by t_2043.FName,isnull(t_2043.FItemID,0)) t left join (select SUM(FQty) as FQty,ISNULL(t2.F_109,0) as FCPXLID from ICInventory t1 inner join t_ICItem t2 on t2.FItemID = t1.FItemID left join t_Item_2043  t_2043  on t2.F_109=t_2043.FItemID group by ISNULL(t2.F_109,0)) t2 on t.FCPXLID=t2.FCPXLID";
                Lg.e("SQL"+SQL);
                sta = conn.prepareStatement(SQL);
                rs = sta.executeQuery();
                WebResponse webResponse = new WebResponse();
                if(rs!=null){
                    while (rs.next()) {
                        WebResponse.SoldOutSortBean bean = webResponse.new SoldOutSortBean();
                        bean.FNum = rs.getString("数量");
                        bean.FQty = rs.getString("当前库存");
                        bean.FMoney = rs.getString("金额");
                        bean.FProductType = rs.getString("产品系列");
                        container.add(bean);
                    }
                }

                webResponse.state = true;
                webResponse.size = container.size();
                webResponse.backString = "库存查询成功";
                webResponse.soldOutSortBeans = container;
                Lg.e("库存查询成功：",webResponse);
                response.getWriter().write(gson.toJson(webResponse));
            } catch (SQLException e) {
                e.printStackTrace();
                Lg.e("数据库错误：","数据库错误："+e.toString());
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));
//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Lg.e("数据库错误：","数据库错误："+e.toString());
                response.getWriter().write(gson.toJson(new WebResponse(false,"数据库错误："+e.toString())));

//                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
