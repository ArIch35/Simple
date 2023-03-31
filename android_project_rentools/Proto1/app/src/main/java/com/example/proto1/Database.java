package com.example.proto1;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;
import java.util.Random;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database extends SQLiteOpenHelper{
    private static final String database_name = "database.db";
    private static final String EQUAL_SIGN = "=";

    public Database(@Nullable Context context) {
        super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry = "create table test (value integer primary key autoincrement)";
        sqLiteDatabase.execSQL(qry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop Table If Exists test");
        onCreate(sqLiteDatabase);
    }

    public SQLiteDatabase getDatabase(){
        try {
            return getReadableDatabase();
        }catch (Exception e){
            return null;
        }
    }

    public boolean checkIfConnectionSuccess(){
        return getDatabase() != null;
    }

    public Email getEmailFromDatabase(String usernameFromScreen) {
        String query = "SELECT * FROM Email WHERE " + getComparisonQuery("emailAddress",EQUAL_SIGN,usernameFromScreen);
        Cursor value = getDatabase().rawQuery(query,null);

        if(value.moveToFirst())
            return new Email(value.getString(0),value.getString(1));
        value.close();
        return null;

    }

    public User getUserFromDatabase(String usernameFromScreen) {
        String query = "SELECT * FROM User WHERE " + getComparisonQuery("emailAddress",EQUAL_SIGN,usernameFromScreen);
        Cursor value = getDatabase().rawQuery(query,null);

        if(value.moveToFirst())
            return new User(value.getInt(0),getEmailFromDatabase(value.getString(4)), value.getString(1),value.getString(2),value.getBlob(3),value.getString(5),value.getInt(6),value.getString(7),value.getInt(8));
        value.close();
        return null;
    }

    public User getUserFromDatabase(int userID) {
        String query = "SELECT * FROM User WHERE " + getComparisonQuery("userID",EQUAL_SIGN,userID);
        Cursor value = getDatabase().rawQuery(query,null);

        if(value.moveToFirst())
            return new User(value.getInt(0),getEmailFromDatabase(value.getString(4)), value.getString(1),value.getString(2),value.getBlob(3),value.getString(5),value.getInt(6),value.getString(7),value.getInt(8));
        value.close();
        return null;
    }

    private boolean convertIntToBool(int value){
        switch (value){
            case 0: return false;
            case 1: return true;
        }
        return false;
    }

    public List<House> getValidHouseListFromDatabase(String city,User this_user){
        List<House> temp_holder = new ArrayList<>();
        String query = validHouseQueryBuilder(this_user);

        if (city != null && !city.equals("")){
            query = query + " AND " + getComparisonQuery("city",EQUAL_SIGN,city);
        }

        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            do {
                temp_holder.add(new House(value.getInt(0),value.getString(1),value.getString(2),
                        value.getString(3),getUserFromDatabase(value.getInt(4)),value.getInt(5),
                        value.getString(6),value.getInt(7),value.getInt(8),convertIntToBool(value.getInt(9)),
                        convertIntToBool(value.getInt(10)),getHouseImages(value.getInt(0))));
            } while (value.moveToNext());
        }
        value.close();

        return temp_holder;
    }

    public List<House> getOwnerHouseListFromDatabase(User this_user){
        List<House> temp_holder = new ArrayList<>();
        String query = "SELECT * FROM House WHERE " + getComparisonQuery("ownerID",EQUAL_SIGN, this_user.getUserID());
        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            do {
                temp_holder.add(new House(value.getInt(0),value.getString(1),value.getString(2),
                        value.getString(3),getUserFromDatabase(value.getInt(4)),value.getInt(5),
                        value.getString(6),value.getInt(7),value.getInt(8),convertIntToBool(value.getInt(9)),
                        convertIntToBool(value.getInt(10)),getHouseImages(value.getInt(0))));
            } while (value.moveToNext());
        }
        value.close();

        return temp_holder;
    }

    private String validHouseQueryBuilder(User this_user){
        if (this_user.getUserRole() == User.Role.CUSTOMER)
            return "SELECT * FROM House";
        else if (this_user.getUserRole() == User.Role.OWNER){
            return "SELECT * FROM House WHERE " + getComparisonQuery("ownerID","!=",this_user.getUserID());
        }
        return "";
    }

    public House getWishlistFromDatabase(String houseID){
        String query = "SELECT * FROM House WHERE " + getComparisonQuery("houseID",EQUAL_SIGN,houseID);
        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            return new House(value.getInt(0),value.getString(1),value.getString(2),
                    value.getString(3),getUserFromDatabase(value.getInt(4)),value.getInt(5),
                    value.getString(6),value.getInt(7),value.getInt(8),convertIntToBool(value.getInt(9)),
                    convertIntToBool(value.getInt(10)),getHouseImages(value.getInt(0)));
        }
        value.close();
        return null;
    }

    public House getHouseFromDatabase(int houseID){
        String query = "SELECT * FROM House WHERE " + getComparisonQuery("houseID",EQUAL_SIGN,houseID);
        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            return new House(value.getInt(0),value.getString(1),value.getString(2),
                    value.getString(3),getUserFromDatabase(value.getInt(4)),value.getInt(5),
                    value.getString(6),value.getInt(7),value.getInt(8),convertIntToBool(value.getInt(9)),
                    convertIntToBool(value.getInt(10)),getHouseImages(value.getInt(0)));
        }
        value.close();

        return null;
    }

    public boolean isOwnerHouse(int ownerID,int houseID){
        String query = "SELECT houseID FROM House WHERE " + getComparisonQuery("ownerID",EQUAL_SIGN,ownerID);
        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            if (value.getInt(0) == houseID)
                return true;
        }
        value.close();

        return false;
    }

    public ArrayList<House> getSearchResultFromDatabase(String city){
        ArrayList<House> temp_holder = new ArrayList<>();

        String query = "SELECT * FROM House";

        if (city != null){
            query += " WHERE " + getComparisonQuery("city",EQUAL_SIGN,city);
        }

        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            do {
                temp_holder.add(new House(value.getInt(0),value.getString(1),value.getString(2),
                        value.getString(3),getUserFromDatabase(value.getInt(4)),value.getInt(5),
                        value.getString(6),value.getInt(7),value.getInt(8),convertIntToBool(value.getInt(9)),
                        convertIntToBool(value.getInt(10)),getHouseImages(value.getInt(0))));
            } while (value.moveToNext());
        }
        value.close();

        return temp_holder;
    }

    public List<String> getCityResultFromDatabase(String text){
        ArrayList<String> temp_holder = new ArrayList<>();

        String query = "SELECT DISTINCT city FROM House WHERE city LIKE '%" + text + "%'";

        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            do {
                temp_holder.add(value.getString(0));
            } while (value.moveToNext());
        }
        value.close();

        return temp_holder;
    }

    public Integer getMaxHouseSize(){
        String query = "SELECT MAX(houseSize) FROM House";
        Cursor value = getDatabase().rawQuery(query,null);
        if (value.moveToFirst()) {
            return value.getInt(0);
        }
        value.close();
        return 0;
    }

    public Integer getMaxHousePrice(){
        String query = "SELECT MAX(housePrice) FROM House";
        Cursor value = getDatabase().rawQuery(query,null);
        if (value.moveToFirst()) {
            return value.getInt(0);
        }
        value.close();
        return 0;
    }

    @SuppressLint("SuspiciousIndentation")
    public Pair<Boolean,List<House>> getFilterResultFromDatabase(String city, Pair<Integer,Integer> prices, String dateDrop,
                                                                 String date, Integer size, String sort, String order, User this_user){
        ArrayList<House> temp_holder = new ArrayList<>();
        ArrayList<String> all_query = new ArrayList<>(Arrays.asList(getCityFilterQuery(city),getPriceFilterQuery(prices)
                ,getDurationFilterQuery(dateDrop),getDateFilterQuery(date),getSizeFilterQuery(size)
        ));

        String notOwner = " AND " + getComparisonQuery("ownerID","!=",this_user.getUserID());

        Pair<Boolean,String> filterQueryResult = getFilterQuery(all_query);

        if (!filterQueryResult.first && sort == null) {
            return new Pair<>(false, getValidHouseListFromDatabase(city, this_user));
        }

        String query = filterQueryResult.second;


        if (filterQueryResult.first)
            query += notOwner;

        if(sort != null)
        query += getSortFilterQuery(sort,order);

        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            do {
                temp_holder.add(new House(value.getInt(0),value.getString(1),value.getString(2),
                        value.getString(3),getUserFromDatabase(value.getInt(4)),value.getInt(5),
                        value.getString(6),value.getInt(7),value.getInt(8),convertIntToBool(value.getInt(9)),
                        convertIntToBool(value.getInt(10)),getHouseImages(value.getInt(0))));
            } while (value.moveToNext());
        }
        value.close();

        if (temp_holder.isEmpty())
            return new Pair<>(false, getValidHouseListFromDatabase(city,this_user));

        return new Pair<>(true,temp_holder);
    }

    private Pair<Boolean,String> getFilterQuery(ArrayList<String> all_query){
        StringBuilder query_head = new StringBuilder("SELECT * FROM House ");
        ArrayList<String> temp_query = new ArrayList<>();

        for (String query: all_query){
            if (query != null)
                temp_query.add(query);
        }

        if (temp_query.isEmpty()){
            return new Pair<>(false,query_head.toString());
        }
        query_head.append("WHERE ");

        for (String query: temp_query){
            query_head.append(query);
            boolean is_end_of_list = temp_query.indexOf(query) == temp_query.size() - 1;

            if (!is_end_of_list && query != null)
                query_head.append(" AND ");
        }
        return new Pair<>(true,query_head.toString());
    }

    private String getCityFilterQuery(String city){
        if (city == null || city.equals(""))
            return null;

        return getComparisonQuery("city",EQUAL_SIGN,city);
    }

    private String getPriceFilterQuery(Pair<Integer,Integer> prices){
        if (prices.first == 0 && prices.second == 0)
            return null;

        return getBetweenQuery("housePrice",prices.first,prices.second);
    }

    private String getDurationFilterQuery(String value){
        String duration = "rentDuration ";
        if (value == null)
            return null;
        switch (value){
            case "< 6 Months":
                return getComparisonQuery(duration,"<",6);
            case "6 - 12 Month":
                return getBetweenQuery(duration,6,12);
            case "13 - 18 Month":
                return getBetweenQuery(duration,13,18);
            case "19 - 24 Month":
                return getBetweenQuery(duration,19,24);
            case "> 24 Months":
                return getComparisonQuery(duration,">",24);
        }
        return null;
    }

    private String getSortFilterQuery(String value,String order){
        String sort = " ORDER BY ";
        if (value == null)
            return null;
        if (order == null)
            order = "ASC";
        switch (value){
            case "Price":
                return sort + "housePrice " + order;
            case "Duration":
                return sort + "rentDuration " + order;
            case "Date Available":
                return sort + "houseAvailable " + order;
            case "Size":
                return sort + "houseSize " + order;
            default:
                return "";
        }
    }

    private String getDateFilterQuery(String value) {
        if (value == null)
            return null;
        return getComparisonQuery("houseAvailable","<=",value);
    }

    private String getSizeFilterQuery(Integer value){
        if (value == 0)
            return null;
        return getComparisonQuery("houseSize",">=",value);
    }

    private String getBetweenQuery(String column, Integer first, Integer second){
        return column + " BETWEEN " + first + " AND " + second;
    }

    private String getComparisonQuery(String column, String comparator, Integer first){
        return column + " " + comparator + " " + first;
    }

    private String getComparisonQuery(String column, String comparator, String first){
        return column + " " + comparator + " '" + first +"'";
    }

    private ArrayList<byte[]> getHouseImages(int ID){
        ArrayList<byte[]> temp_holder = new ArrayList<>();
        String query = "SELECT * FROM HouseImage WHERE " + getComparisonQuery("houseID",EQUAL_SIGN,ID);
        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            do {
                temp_holder.add(value.getBlob(1));
            } while (value.moveToNext());
        }
        value.close();

        return temp_holder;
    }

    private boolean checkIfChatExist(User receiver, User sender){
        String query = "SELECT * FROM ChatList WHERE " + getComparisonQuery("userID",EQUAL_SIGN,receiver.getUserID()) +
                " AND " + getComparisonQuery("chatPartnerID",EQUAL_SIGN,sender.getUserID());
        Cursor value = getDatabase().rawQuery(query,null);

        boolean exist = value.moveToFirst();
        value.close();

        return exist;
    }

    public boolean addToChatQuery(User sender, User receiver){
        if (sender.getUserID() == receiver.getUserID())
            return false;

        if (checkIfChatExist(receiver,sender))
            return true;

        try {
            String query = "INSERT INTO ChatList(userID,chatPartnerID,chatID) \n" +
                    "SELECT " + sender.getUserID() + "," + receiver.getUserID() +
                    "," + randomNumberMaker(generateRandomNumber()) +"\n" +
                    "WHERE NOT EXISTS(SELECT 1 FROM ChatList WHERE " + getComparisonQuery("userID",EQUAL_SIGN,sender.getUserID())
                    + " AND " + getComparisonQuery("chatPartnerID",EQUAL_SIGN,receiver.getUserID()) + ")";
            getDatabase().execSQL(query);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    private int generateRandomNumber(){
        Random rand = new Random();
        int n = rand.nextInt(900000 + 1);
        return n+100000;
    }

    private int randomNumberMaker(int random){
        String query = "SELECT chatID FROM ChatList WHERE chatID = "+ random;
        Cursor value = getDatabase().rawQuery(query,null);
        if (value.moveToFirst()){
            random += generateRandomNumber();
            randomNumberMaker(random);
        }
        value.close();
        return random;
    }

    public boolean hasItem(User thisUser, House thisHouse){
        String query = "SELECT * FROM Wishlist WHERE " + getComparisonQuery("userID",EQUAL_SIGN,thisUser.getUserID()) +
                " AND " + getComparisonQuery("houseID",EQUAL_SIGN,thisHouse.getHouseID());
        Cursor value = getDatabase().rawQuery(query,null);

        boolean exist = value.moveToFirst();
        value.close();

        return exist;
    }

    public void removeFromWishlistQuery(User thisUser, House thisHouse){
        String query = "DELETE FROM Wishlist Where " + getComparisonQuery("userID",EQUAL_SIGN,thisUser.getUserID())
                 + " AND " + getComparisonQuery("houseID",EQUAL_SIGN,thisHouse.getHouseID());
        getDatabase().execSQL(query);
    }

    public void addToWishListQuery(User thisUser, House thisHouse){
        String query = "INSERT INTO Wishlist(userID,houseID) \n" +
                "SELECT " + thisUser.getUserID() + ", '" + thisHouse.getHouseID() + "'\n" +
                "WHERE NOT EXISTS(SELECT 1 FROM Wishlist WHERE " +
                getComparisonQuery("userID",EQUAL_SIGN,thisUser.getUserID())
                + " AND " + getComparisonQuery("houseID",EQUAL_SIGN,thisHouse.getHouseID()) +")";
        getDatabase().execSQL(query);
    }

    public ArrayList<String> getWishlistQuery(User thisUser){
        ArrayList<String> temp_holder = new ArrayList<>();
        String query = "SELECT * FROM Wishlist WHERE " + getComparisonQuery("userID",EQUAL_SIGN,thisUser.getUserID());
        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            do {
                temp_holder.add(value.getString(1));
            } while (value.moveToNext());
        }
        value.close();

        return temp_holder;
    }

    public List<Message> getChatMessagesFromDatabase(User sender, User receiver){
        ArrayList<Message> temp_holder = new ArrayList<>();

        int chat_id = getChatIDFromDatabase(sender,receiver);

        String query = "SELECT * FROM MessageList WHERE " + getComparisonQuery("chatID",EQUAL_SIGN,chat_id) +" ORDER BY exactDate ASC";

        Cursor value = getDatabase().rawQuery(query,null);

        if (value.moveToFirst()) {
            do {
                addToMessageList(temp_holder, value, sender);
            } while (value.moveToNext());
        }
        value.close();
        return temp_holder;
    }

    private void addToMessageList(ArrayList<Message> temp_holder, Cursor value, User thisUser) {
        Message temp_message = new Message(value.getString(1), value.getString(2), value.getInt(3), value.getString(4));
        setMessageTag(temp_message,thisUser);
        temp_holder.add(temp_message);
    }

    private void setMessageTag(Message thisMessage,User thisUser){
        if (thisUser.getUserID() == thisMessage.getSenderID()) {
            thisMessage.setTag("sender");
            return;
        }
        thisMessage.setTag("receiver");
    }

    public int getChatIDFromDatabase(User sender, User receiver) {
        int chat_id = 0;
        String query = "SELECT * FROM ChatList WHERE " + getComparisonQuery("userID",EQUAL_SIGN,sender.getUserID()) +
                " AND " + getComparisonQuery("chatPartnerID",EQUAL_SIGN,receiver.getUserID()) + " OR " +
                getComparisonQuery("userID",EQUAL_SIGN,receiver.getUserID())
                 + " AND " + getComparisonQuery("chatPartnerID",EQUAL_SIGN,sender.getUserID());
        Cursor value = getDatabase().rawQuery(query,null);

        if(value.moveToFirst()){
            chat_id = value.getInt(2);
        }
        value.close();
        return chat_id;
    }

    public void addToMessageListQuery(int chat_id, Message message){
        String query = "INSERT INTO MessageList(chatID,message,exactTime,senderID,exactDate) \n" +
                "VALUES (" + chat_id + ", '" + message.getMessage() + "', '" +
                message.getCurrentTime() + "'," + message.getSenderID() + ",'"+ message.getMessage_delivery_date() +"');";
        getWritableDatabase().execSQL(query);
    }

    public ArrayList<Chat> getChatPartners(User this_user){
        ArrayList <Chat> temp_holder = new ArrayList<>();
        int partnerID;
        String query = "SELECT * FROM ChatList WHERE " + getComparisonQuery("userID",EQUAL_SIGN,this_user.getUserID()) +
                " OR " + getComparisonQuery("chatPartnerID",EQUAL_SIGN,this_user.getUserID());

        Cursor value = getDatabase().rawQuery(query,null);
        if(value.moveToFirst()){
            do {
                partnerID = getPartnerID(this_user, value);
                temp_holder.add(new Chat(getUserFromDatabase(partnerID),value.getInt(2),
                        getLastMessage(value.getInt(2))));
            } while (value.moveToNext());
        }
        return temp_holder;
    }

    private Message getLastMessage(int chatId){
        Message temp_message = new Message("");
        String query = "SELECT * FROM MessageList WHERE " + getComparisonQuery("chatID",EQUAL_SIGN,chatId) +
                " ORDER BY exactDate DESC LIMIT 1";

        Cursor value = getDatabase().rawQuery(query,null);
        if(value.moveToFirst()){
            temp_message = new Message(value.getString(1), value.getString(2), value.getInt(3), value.getString(4));
        }
        value.close();
        return temp_message;
    }

    private int getPartnerID(User this_user, Cursor value) {
        int userID1;
        int userID2;
        userID1 = value.getInt(0);
        userID2 = value.getInt(1);

        if (userID1 != this_user.getUserID()){
            return userID1;
        }
        return userID2;
    }

    public void updateUserImage(int userID,byte[] newImage){
        ContentValues values = new ContentValues();
        String argument = "userID = " + userID;
        values.put("userImage",newImage);
        getDatabase().update("User",values,argument,null);
        getDatabase().close();
    }

    public void updateUserPassword(String email,String password){
        String query = "UPDATE Email SET  emailPassword = '" + password +"' WHERE " + getComparisonQuery("emailAddress",EQUAL_SIGN,email);
        getDatabase().execSQL(query);
        getDatabase().close();
    }

    public void updateUserInfo(User user){
        ContentValues values = new ContentValues();
        String argument = "userID = " + user.getUserID();
        values.put("firstName",user.getFirstName());
        values.put("secondName",user.getSecondName());
        values.put("firstName",user.getFirstName());
        values.put("age",user.getUserAge());
        values.put("gender",user.getUserGender());
        values.put("telephone",user.getUserTelephone());
        getDatabase().update("User",values,argument,null);

    }
}
