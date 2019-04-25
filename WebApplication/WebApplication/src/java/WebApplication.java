/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//test
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author RajonZhang
 */
@WebServlet(urlPatterns = {"/WebApplication"})
public class WebApplication extends HttpServlet {

    
    MongoCollection<Document> hostCollection;
    MongoCollection<Document> guestCollection;
    MongoClientURI uri = new MongoClientURI(
    "mongodb://kaidiz:2510685@cluster0-shard-00-00-k7yzj.mongodb.net:27017,cluster0-shard-00-01-k7yzj.mongodb.net:27017,cluster0-shard-00-02-k7yzj.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");

    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase database = mongoClient.getDatabase("MyDataBase");


    
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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String singleString = request.getParameter("parameter");
        PrintWriter out = response.getWriter();
//        out.println("<!DOCTYPE html>");
//        out.println("<html>");
//        out.println("<head>");
//        out.println("<title>Servlet NewServlet</title>");            
//        out.println("</head>");
//        out.println("<body>");
//        out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
//        out.println("<h1>"+singleString+"</h1>");
        
        
        
        
        
        
        
        
        
        

//        out.println("<h1>"+paraArray[0]+"</h1>");
        if(singleString.equals("newHostPost")){
            out.println("add host");
            newHostPost(request);
        }
        if(singleString.equals("newGuestPost")){
            out.println("Add Guest Record");
            newGuestPost(request);
        }
        if(singleString.equals("getAllHostPost")){
            if(hostCollection==null){
                hostCollection = getAllHostPost();
            }
            List<String> allHostPostJson = getListStringFromCollection(hostCollection);
            for(int i=0;i<allHostPostJson.size();i++){
//                out.println("<h5>"+each+"</h5>");
                if(i<allHostPostJson.size()-1)
                    out.write(allHostPostJson.get(i)+",");
                else
                    out.write(allHostPostJson.get(i));
            }
            out.flush();
            
        }
        if(singleString.equals("getAllGuestPost")){
            if(guestCollection==null){
                guestCollection = getAllGuestPost();
            }
            List<String> allGuestPostJson = getListStringFromCollection(guestCollection);
            for(int i=0;i<allGuestPostJson.size();i++){
//                out.println("<h5>"+each+"</h5>");
                if(i<allGuestPostJson.size()-1)
                    out.write(allGuestPostJson.get(i)+",");
                else
                    out.write(allGuestPostJson.get(i));
            }
            out.flush();
        }
        if(singleString.equals("GuestClickPost")){
            GuestClickPost(request);
        }
        if(singleString.equals("HostClickPost")){
            HostClickPost(request);
        }
        if(singleString.equals("getHistory")){
            String username = request.getParameter("username");
            List<String> history = getHistory(username);
            for(int i=0;i<history.size();i++){
//                out.println("<h5>"+each+"</h5>");
                if(i<history.size()-1)
                    out.write(history.get(i)+",");
                else
                    out.write(history.get(i));
            }
        }
        if(singleString.equals("getComment")){
            List<String> comment = getComment(request.getParameter("username"));
            for(int i=0;i<comment.size();i++){
//                out.println("<h5>"+each+"</h5>");
                if(i<comment.size()-1){
                    out.write(comment.get(i)+",");
                }
                else{
                    out.write(comment.get(i));
                }
            }
        }
        if(singleString.equals("makeComment")){
            makeComment(request);
        }
        if(singleString.equals("getPost")){
            List<String> list = getPost(request.getParameter("Username"));
            if(list.size()==0){
            }
            else{
                for(int i=0;i<list.size()-1;i++){
                    out.write(list.get(i)+",");
                }
                out.write(list.get(list.size()-1));
            }
        }
        

        
//        out.println("</body>");
//        out.println("</html>");
        
        //  http://localhost:8080/WebApplication/WebApplication?parameter=newGuestPost&foodType=Chinese&extraInformation=no extra information&preferDate=20190409
        //  http://localhost:8080/WebApplication/WebApplication?parameter=getAllGuestPost
        
     
        //  http://localhost:8080/WebApplication/WebApplication?parameter=newHostPost&address1=3913 Nantasket Street&address2=1&postCode=15207&city=Pittsburgh&state=PA&foodType=Chinese&extraInformation=no information&preferDate=20190418&number=7&owner=kaidiz&lat=40.96&lng=76.22
        //  http://localhost:8080/WebApplication/WebApplication?parameter=getAllHostPost
        
        

        //http://localhost:8080/WebApplication/WebApplication?parameter=getHistory&username=kaidiz
        
        //http://localhost:8080/WebApplication/WebApplication?parameter=GuestClickPost&postID=1&guestUsername=kaidiz

        
        
        
       //http://localhost:8080/WebApplication/WebApplication?parameter=getComment&username=kaidiz
       //http://localhost:8080/WebApplication/WebApplication?parameter=makeComment&rate=4.5&comment=comment&Username=kaidiz 
        

        //http://localhost:8080/WebApplication/WebApplication?parameter=getPost&Username=kaidiz
        
    }
    
    public void newHostPost(HttpServletRequest request){
        
        MongoCollection<Document> collection = database.getCollection("HostPost");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String postCode = request.getParameter("postCode");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String foodType = request.getParameter("foodType");
        String extraInformation = request.getParameter("extraInformation");
        String preferDate = request.getParameter("preferDate");
        int number = Integer.parseInt(request.getParameter("number"));
        String owner = request.getParameter("owner");
        String lat = "40.4273999";
        String lng = "-79.9403981";
        if(request.getParameter("lat")!=null){
            lat = request.getParameter("lat");
        }
        if(request.getParameter("lng")!=null){
            lng = request.getParameter("lng");
        }
        Document coordinates = new Document("lat", lat)
                               .append("lng", lng);
        
        int count = (int) collection.count();
        int postID = count+1;
        
        Document address = new Document("address1", address1)
                .append("address2", address2)
                .append("postCode", postCode)
                .append("city", city)
                .append("state", state);
        
        //shi jian xu yao gai yi xia ***********************************************
        Document JsonInfo = new Document("postID", postID)
                            .append("address", address)
                            .append("foodType", foodType)
                            .append("extraInformation", extraInformation)
                            .append("preferDate", preferDate)
                            .append("number", number)
                            .append("guest", Arrays.asList())
                            .append("display", "True")
                            .append("owner", owner)
                            .append("coordinates", coordinates);
        
        collection.insertOne(JsonInfo);
        hostCollection = null;
        
        MongoCollection<Document> UserPost = database.getCollection(owner+"Post");
        Document OwnerPost = new Document("postID", postID)
                            .append("address", address)
                            .append("foodType", foodType)
                            .append("extraInformation", extraInformation)
                            .append("preferDate", preferDate)
                            .append("number", number)
                            .append("guest", Arrays.asList())
                            .append("display", "True")
                            .append("owner", owner)
                            .append("coordinates", coordinates);
        UserPost.insertOne(OwnerPost);
        
    }
    
    public void newGuestPost(HttpServletRequest request){
        MongoCollection<Document> collection = database.getCollection("GuestPost");
        
        int count = (int) collection.count();
        
        String foodType = request.getParameter("foodType");
        String extraInformation = request.getParameter("extraInformation");
        String preferDate = request.getParameter("preferDate");
        int postID = count+1;
        
        Document JsonInfo = new Document("postID", postID)
                            .append("foodType", foodType)
                            .append("extraInformation", extraInformation)
                            .append("preferDate", preferDate)
                            .append("host", -1)
                            .append("display", "True");
        collection.insertOne(JsonInfo);
        guestCollection = null;
    }
    
    public MongoCollection<Document> getAllHostPost(){
        
        MongoCollection<Document> collection = database.getCollection("HostPost");
        return collection;
        
    }
    
    public List<String> getListStringFromCollection(MongoCollection<Document> hostCollection){
        List<String> allHostPostString = new ArrayList<>();

        MongoCursor<Document> cursor = hostCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                allHostPostString.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        return allHostPostString;
    }
    
    public MongoCollection<Document> getAllGuestPost(){

        MongoCollection<Document> collection = database.getCollection("GuestPost");
        
        return collection;
    }
    

    
    public void GuestClickPost(HttpServletRequest request){
        int postID = Integer.parseInt(request.getParameter("postID"));
        String guestUsername = request.getParameter("guestUsername");
        
        if(hostCollection==null){
            hostCollection = getAllHostPost();
        }
        
        Document myDoc = hostCollection.find(eq("postID", postID)).first();
       
        List<String> guestList = (List<String>) myDoc.get("guest");
        String display =  (String) myDoc.get("display");
        
        guestList.add(guestUsername);
        if(guestList.size()==(int)myDoc.get("number")){
            display = "False";
        }
        
        
        
        myDoc.put("display", display);
//        myDoc.put("guest", guestList.toArray());
        hostCollection.deleteOne(eq("postID", postID));
        hostCollection.insertOne(myDoc);
        
        
//        MongoClientURI uri = new MongoClientURI(
//    "mongodb://kaidiz:2510685@cluster0-shard-00-00-k7yzj.mongodb.net:27017,cluster0-shard-00-01-k7yzj.mongodb.net:27017,cluster0-shard-00-02-k7yzj.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
//
//        MongoClient mongoClient = new MongoClient(uri);
//        MongoDatabase database = mongoClient.getDatabase("MyDataBase");
        MongoCollection<Document> History = database.getCollection(guestUsername+"History");
        
        
//        Document JsonInfo = new Document("postID", postID)
//                            .append("foodType", foodType)
//                            .append("extraInformation", extraInformation)
//                            .append("preferDate", preferDate)
//                            .append("host", -1)
//                            .append("display", "True");
        
        
        Document newMyDoc = new Document("postID", myDoc.get(postID))
                            .append("address", myDoc.get("address"))
                            .append("foodType", myDoc.get("foodType"))
                            .append("extraInformation", myDoc.get("extraInformation"))
                            .append("preferDate", myDoc.get("preferDate"))
                            .append("number", myDoc.get("number"))
                            .append("guest", guestList)
                            .append("display", display);
        History.insertOne(newMyDoc);
        
    }
    
    public void HostClickPost(HttpServletRequest request){
        int postID = Integer.parseInt(request.getParameter("postID"));
        int hostID = Integer.parseInt(request.getParameter("hostID"));
        
//        MongoClientURI uri = new MongoClientURI(
//    "mongodb://kaidiz:2510685@cluster0-shard-00-00-k7yzj.mongodb.net:27017,cluster0-shard-00-01-k7yzj.mongodb.net:27017,cluster0-shard-00-02-k7yzj.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
//
//        MongoClient mongoClient = new MongoClient(uri);
//        MongoDatabase database = mongoClient.getDatabase("MyDataBase");
        MongoCollection<Document> collection = database.getCollection("GuestPost");
        Document myDoc = collection.find(eq("postID", postID)).first();
        
        Integer hostIDFromDoc = (Integer) myDoc.get("host");
        String display =  (String) myDoc.get("display");
        
        
        hostIDFromDoc = hostID;
        display = "False";
        
        
        myDoc.put("display", display);
        myDoc.put("host", hostIDFromDoc);
        
        collection.deleteOne(eq("postID", postID));
        collection.insertOne(myDoc);
    }
    
    public void makeComment(HttpServletRequest request){
        
        double rate = Double.parseDouble(request.getParameter("rate"));
        String comment = request.getParameter("comment");
        String Username = request.getParameter("Username");
//        MongoClientURI uri = new MongoClientURI(
//    "mongodb://kaidiz:2510685@cluster0-shard-00-00-k7yzj.mongodb.net:27017,cluster0-shard-00-01-k7yzj.mongodb.net:27017,cluster0-shard-00-02-k7yzj.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
//
//        MongoClient mongoClient = new MongoClient(uri);
//        MongoDatabase database = mongoClient.getDatabase("MyDataBase");
        MongoCollection<Document> collection = database.getCollection(Username+"Comment");
        Document JsonInfo = new Document("rate", rate)
                            .append("comment", comment);
        collection.insertOne(JsonInfo);
        guestCollection = null;
    }
    
    public List<String> getComment(String Username){
//        MongoClientURI uri = new MongoClientURI(
//    "mongodb://kaidiz:2510685@cluster0-shard-00-00-k7yzj.mongodb.net:27017,cluster0-shard-00-01-k7yzj.mongodb.net:27017,cluster0-shard-00-02-k7yzj.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
//
//        MongoClient mongoClient = new MongoClient(uri);
//        MongoDatabase database = mongoClient.getDatabase("MyDataBase");
        MongoCollection<Document> collection = database.getCollection(Username+"Comment");
        List<String> allComment = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                allComment.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        return allComment;
    }
    
    
    public List<String> getHistory(String Username){
//        MongoClientURI uri = new MongoClientURI(
//    "mongodb://kaidiz:2510685@cluster0-shard-00-00-k7yzj.mongodb.net:27017,cluster0-shard-00-01-k7yzj.mongodb.net:27017,cluster0-shard-00-02-k7yzj.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
//
//        MongoClient mongoClient = new MongoClient(uri);
//        MongoDatabase database = mongoClient.getDatabase("MyDataBase");
        MongoCollection<Document> collection = database.getCollection(Username+"History");
        List<String> history = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                history.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        return history;
    }
    
    public List<String> getPost(String Username){
//        MongoClientURI uri = new MongoClientURI(
//    "mongodb://kaidiz:2510685@cluster0-shard-00-00-k7yzj.mongodb.net:27017,cluster0-shard-00-01-k7yzj.mongodb.net:27017,cluster0-shard-00-02-k7yzj.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");
//
//        MongoClient mongoClient = new MongoClient(uri);
//        MongoDatabase database = mongoClient.getDatabase("MyDataBase");
        MongoCollection<Document> collection = database.getCollection(Username+"Post");
        List<String> allPost = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                allPost.add(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        return allPost;
    }
    
    
    
    
    //request.getContextPath()

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
        processRequest(request, response);
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
        processRequest(request, response);
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
