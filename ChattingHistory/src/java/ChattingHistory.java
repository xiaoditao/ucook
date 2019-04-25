/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//1451
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
 
import org.apache.commons.lang3.StringUtils;
 
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 

/**
 *
 * @author RajonZhang
 */
@WebServlet(urlPatterns = {"/ChattingHistory"})
public class ChattingHistory extends HttpServlet {

    
    Map<Integer, List<String>> map = new HashMap<>();
    
    public void test(){
        List<String> l = new ArrayList<>();
        l.add("test1");
        l.add("test2");
        l.add("test3");
        map.put(1, l);
    }

    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String type = request.getParameter("type");
            if(map.getOrDefault(1, new ArrayList<>()).size()==0){
                test();
            }else{
                
            }
            //////////
            if(type.equals("firstReresh")){
                String num = request.getParameter("num");
                List<String> list = map.getOrDefault(Integer.parseInt(num), new ArrayList<>());
                String res = "";
                for(int i=0;i<list.size();i++){
                    res += list.get(i)+"\n";
                }
                out.print(res);
            }
            else if (type.equals("sendMessage")){
                String num = request.getParameter("num");
                String content = request.getParameter("content");
                List<String> list = map.getOrDefault(Integer.parseInt(num), new ArrayList<>());
                list.add(content);
                map.put(Integer.parseInt(num), list);
            }
            else if(type.equals("decrypt")){
                String content = request.getParameter("content");
                Decode des = new Decode();
                String[]  decodeRes = des.decrypt(content);
                out.print(decodeRes[0]+"-"+decodeRes[1]);
            }
        }
    }
    
    //http://localhost:8080/ChattingHistory/ChattingHistory?type=firstReresh&num=1
    //http://localhost:8080/ChattingHistory/ChattingHistory?type=sendMessage&num=1&content=kaidiz:asldlfasdjf
    //http://localhost:8080/ChattingHistory/ChattingHistory?type=encrypt&content=1+kaidiz2
    //http://localhost:8080/ChattingHistory/ChattingHistory?type=decrypt&content=lasjdfa;s

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ChattingHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ChattingHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
