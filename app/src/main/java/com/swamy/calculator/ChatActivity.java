package com.swamy.calculator;

import static android.content.ContentValues.TAG;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.RoomDatabase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swamy.calculator.Database.AppDataBase;
import com.swamy.calculator.Database.Message;
import com.swamy.calculator.databinding.ActivityChatBinding;
import com.swamy.calculator.view.MessageLoadCallBack;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ChatActivity extends AppCompatActivity implements MessageLoadCallBack {
    ActivityChatBinding chatBinding;
    SharedPreferences sharedPreferences;
    FirebaseDatabase database;
    DatabaseReference rootref;
    DatabaseReference chatRef;
    boolean isHadRoot;
    CountDownLatch latchcheckroot;
    boolean isNameSet;
    CountDownLatch checkUserNameLatch;
    String userName;
    String userPlaceOfFriend;
    String userPlace;

     AppDataBase appDataBase;
     List<Message> messagesList;

    RecyclerView recyclerView;
    Adapter adapter;
    ChildEventListener messageValueEventLister;
    ValueEventListener statusLister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());
        int maxWidhtOfEditText=chatBinding.message.getWidth();
        chatBinding.message.setMaxWidth(maxWidhtOfEditText);
        chatBinding.blueScreen.setVisibility(View.GONE);
        chatBinding.detailsTab.setVisibility(View.GONE);
        sharedPreferences =getPreferences(getApplicationContext().MODE_PRIVATE);
        rootref=FirebaseDatabase.getInstance("https://private-chat-app-9495f-default-rtdb.firebaseio.com/").getReference();
        //app database local
        appDataBase=AppDataBase.getInstance(getApplicationContext());


        // firebase

        Log.d(TAG, "onCreate: rootref");
        Log.d(TAG, "onCreate: rootref"+sharedPreferences.getBoolean("isNotSet",true));

        // spinner for user selection

        /***ArrayAdapter<CharSequence> userSpinneradapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.userList, android.R.layout.simple_spinner_item);
        userSpinneradapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        chatBinding.userSpinner.setAdapter(userSpinneradapter);

         ***/

        //chatBinding.userSpinner.setOnItemClickListener();

        recyclerView=chatBinding.recycleView;
        adapter=new Adapter(getApplicationContext());
       // adapter.messages=appDataBase.messageDeo().getAllMessages();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        // linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        fetchMessageFromLocalDb(this);

         messageValueEventLister=new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    Map<String,String> dataMap=(HashMap<String,String>) snapshot.getValue();
                    Log.d(TAG, "onChildAdded: "+dataMap.get("message"));
                    Log.d(TAG, "onChildAdded: "+dataMap.get("time"));
                    Message message=new Message(dataMap.get("message"),dataMap.get("time"),false);
                    updateToLocalDb(message);
                    adapter.insertMessge(message);
                    recyclerView.smoothScrollToPosition(adapter.messages.size());
                }
                catch (Exception e)
                {
                    Log.d(TAG, "onChildAdded: "+e.getMessage());
                }
                try {
                    snapshot.getRef().getParent().removeValue();

                }
                catch (Exception e){
                    Log.d(TAG, "onChildAdded: "+e.getMessage());
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        /***
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    String key=dataSnapshot.getKey();
                    Log.d(TAG, "onDataChange: key"+key);
                    Log.d(TAG, "onDataChanged dtabase ref= "+ dataSnapshot.getRef());
                    String msg=dataSnapshot.child("msg").getValue().toString();
                    String time= dataSnapshot.child("time").getValue().toString();
                    Message message=new Message(msg,time,false);
                    updateToLocalDb(message);
                    adapter.insertMessge(message);
                   recyclerView.smoothScrollToPosition(adapter.messages.size());
                }
                Log.d(TAG, "onDataChange: delete msg ref");
               // DatabaseReference deleteMessageRef=snapshot.getRef();
                //deleteMessageRef.removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: data in msg ref datachange");
            }
        };

         ***/


         statusLister=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("online")){
                    Log.d(TAG, "onDataChange: status changed staus has to change");
                   // Toast.makeText(ChatActivity.this, "status changed", Toast.LENGTH_SHORT).show();
                    chatBinding.satusBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.online));
                    return;
                }
               //Toast.makeText(ChatActivity.this, "status online", Toast.LENGTH_SHORT).show();
                chatBinding.satusBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.offline));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        // it runs only when users are setup their account
        if(!sharedPreferences.getBoolean("isNotSet",true)){
            chatBinding.send.setVisibility(View.VISIBLE);
            chatRef=rootref.child(sharedPreferences.getString("chatRef","default")).getRef();
            userPlace=sharedPreferences.getString("userPlace","user1");
            Log.d(TAG, "onCreate: userp "+userPlace+" "+userPlaceOfFriend);
            userPlaceOfFriend=sharedPreferences.getString("friendUserPlace","user2");
            // what happen when the user 2 was not created account
            chatRef.child("users").child(userPlaceOfFriend).child("status").addValueEventListener(statusLister);
            chatRef.child("users").child(userPlace).child("status").setValue("online");

            chatRef.child("users").child(userPlaceOfFriend).child("messages").addChildEventListener(messageValueEventLister);
            Log.d(TAG, "onCreate: rootref"+sharedPreferences.getBoolean("isNotSet",true));


        }


        chatBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=chatBinding.message.getText().toString();
                if(msg.equals("")) return;
                Log.d(TAG, "onClick: error 1");
                Log.d(TAG, "onClick: user place"+userPlace);

                DatabaseReference tempMessageRef=chatRef.child("users").child(userPlace).child("messages").push().getRef();
                String time=getTime();

                //FetechedMessageObj fetechedMessageObj=new FetechedMessageObj(msg,time);
                //tempMessageRef.child("messageBody").setValue(fetechedMessageObj);

                Map<String,String> messageMap=new HashMap<>();
                messageMap.put("message",msg);
                messageMap.put("time",time);
                tempMessageRef.setValue(messageMap);
                //tempMessageRef.child("time").setValue(time);
                //Log.d(TAG, "onClick: ");
                //tempMessageRef.child("msg").setValue(msg);
                Message message=new Message(msg,time,true);
                updateToLocalDb(message);
                adapter.insertMessge(message);
                recyclerView.smoothScrollToPosition(adapter.messages.size());
                chatBinding.message.setText("");



            }
        });
        //settign tab
        chatBinding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sharedPreferences.getBoolean("isNotSet",true)){
                    return;
                }
                boolean isShowed=chatBinding.detailsTab.isShown();
                if(isShowed){
                    chatBinding.detailsTab.setVisibility(View.GONE);
                    return;
                }
                chatBinding.detailsTab.setVisibility(View.VISIBLE);

                chatBinding.codeWarning.setVisibility(View.VISIBLE);
                chatBinding.nameWarning.setVisibility(View.VISIBLE);
            }
        });
        chatBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rootCode=chatBinding.rootcode.getText().toString();
                if(rootCode.equals("")){
                    chatBinding.codeWarning.setVisibility(View.VISIBLE);
                    chatBinding.codeWarning.setText("Please fill the field");
                    return;
                }
                if(chatBinding.nameWarning.getText().toString().equals("")){
                    chatBinding.nameWarning.setVisibility(View.VISIBLE);
                    return;
                }

                Log.d(TAG, "onClick: "+"in save btn");
                userName=chatBinding.username.getText().toString();

                if(!sharedPreferences.getBoolean("isNotSet",true)){
                    chatBinding.detailsTab.setVisibility(View.GONE);
                    return;
                }
                Log.d(TAG, "onClick: "+"in save btn");
                chatBinding.codeWarning.setVisibility(View.VISIBLE);

                checkRoot(rootCode,userName);

                chatBinding.detailsTab.setVisibility(View.GONE);

            }
        });

        //for turn off warning on rootcode
        chatBinding.rootcode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                        chatBinding.codeWarning.setVisibility(View.GONE);
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        //chatBinding.codeWarning.setVisibility(View.GONE);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //chatBinding.codeWarning.setVisibility(View.GONE);
                    }
                });






        chatBinding.goDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    recyclerView.smoothScrollToPosition(adapter.messages.size());
                }
                catch (Exception e){
                    Log.d(TAG, "on floating action clicked "+e.getMessage());
                }
            }
        });




    }



    private void updateToLocalDb(Message message) {

        ExecutorService executorService=Executors.newSingleThreadExecutor();
        Handler handler=new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                appDataBase.messageDeo().insert(message);

                //handler.post(() -> Toast.makeText(ChatActivity.this, "message added ", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void fetchMessageFromLocalDb(MessageLoadCallBack messageLoadCallBack) {
       // final List<Message>[] messageList = new List[]{null};
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        Handler handler=new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Message> messageList=appDataBase.messageDeo().getAllMessages();
                    messageLoadCallBack.onLoaded(messageList);

                }
                catch (Exception e){
                    messageLoadCallBack.onLoadFailed(e.getMessage());
                }
                // dataAdded succuessfully
               // handler.post(() -> Toast.makeText(ChatActivity.this, "data Loaded data size=", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private String getTime() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a", Locale.getDefault());
        String currentTime=timeFormat.format(calendar.getTime());
        return currentTime;
    }



    private void checkIsUser1ExitAndSetUser(String name) {
        // checkUserNameLatch=new CountDownLatch(1);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.child("users").hasChild("user1")){
                    String user1Name=snapshot.child("users").child("user1").getValue().toString();
                    if(user1Name.equals(name))
                    {
                        chatRef.child("users").child("user1").child("username").setValue(name);
                        chatRef.child("users").child("user1").child("status").setValue("online");

                        return;
                    }
                    chatRef.child("users").child("user2").child("username").setValue(name);
                    chatRef.child("users").child("user2").child("status").setValue("online");
                    Toast.makeText(ChatActivity.this, "successfully setup", Toast.LENGTH_SHORT).show();


                    /***userPlaceOfFriend="user1";
                    userPlace="user2";***/
                    SharedPreferences.Editor shared=sharedPreferences.edit();
                    shared.putString("friendUserPlace","user1");
                    shared.putString("userPlace","user2");
                    shared.apply();

                }
                else{
                    chatRef.child("users").child("user1").child("username").setValue(name);
                    chatRef.child("users").child("user1").child("status").setValue("online");

                    //temporary ly adding details for avoid null pointer exeception on status for eventlister
                    chatRef.child("users").child("user2").child("username").setValue(name);
                    chatRef.child("users").child("user2").child("status").setValue("offline");
                    Toast.makeText(ChatActivity.this, "successfully setup", Toast.LENGTH_SHORT).show();

                    /***userPlaceOfFriend="user2";
                    userPlace="user1";***/
                    SharedPreferences.Editor shared=sharedPreferences.edit();
                    shared.putString("friendUserPlace","user2");
                    shared.putString("userPlace","user1");
                    shared.apply();


                    isNameSet=true;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
              //  Toast.makeText(ChatActivity.this, "failed"+error.getMessage(), Toast.LENGTH_SHORT).show();
                isNameSet=false;
                checkUserNameLatch.countDown();
            }

        });
        try {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        catch (Exception e){
            Log.d(TAG, "checkIsUser1ExitAndSetUser: exeception "+e.getMessage());
        }


    }



    private void  checkRoot(String rootCode,String name) {

      //  CompletableFuture<Boolean> future=new CompletableFuture<>();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(rootCode)) {

                   // Toast.makeText(ChatActivity.this, "Root exists", Toast.LENGTH_SHORT).show();
                    chatRef=rootref.child(rootCode);
                    chatBinding.send.setVisibility(View.VISIBLE);
                    checkIsUser1ExitAndSetUser(name);
                    SharedPreferences.Editor sharedEditor=sharedPreferences.edit();
                    sharedEditor.putBoolean("isNotSet",false);
                    sharedEditor.putString("chatRef",rootCode);
                    sharedEditor.apply();
                    Log.d(TAG, "onClick: "+"in save btn in rootcheck successf data updated");


                } else {
                    chatBinding.codeWarning.setText("Entered code was wrong");
                    isHadRoot = false;
                    SharedPreferences.Editor sharedEditor=sharedPreferences.edit();
                    sharedEditor.putBoolean("isNotSet",true);
                    sharedEditor.apply();
                    Log.d(TAG, "onClick: "+"in save btn faild");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isHadRoot = false;
                SharedPreferences.Editor sharedEditor=sharedPreferences.edit();
                sharedEditor.putBoolean("isNotSet",true);
                sharedEditor.apply();
                Log.d(TAG, "onClick: "+"in save btn + faild to update ");
              //  Toast.makeText(ChatActivity.this, "Failed to send to Firebase " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
        try {
            chatRef.child("users").child(userPlace).child("status").setValue("offline");
        }
        catch (NullPointerException nullPointerException){
            Log.d(TAG, "onPause: nullpointer exception due to no ref found ");

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
       try {
           chatRef.child("users").child(userPlace).child("status").setValue("offline");
       }
       catch (NullPointerException nullPointerException){
           Log.d(TAG, "onPause: nullpointer exception due to no ref found ");

       }
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            chatRef.child("users").child(userPlace).child("status").setValue("offline");
            chatBinding.blueScreen.setVisibility(View.VISIBLE);
        }catch (NullPointerException nullPointerException){
            Log.d(TAG, "onPause: nullpointer exception due to no ref found ");
        }



    }

    @Override
    public void onLoaded(List<Message> fetchedMessages) {
        adapter.messages=fetchedMessages;
    }

    @Override
    public void onLoadFailed(String ErrorMessage) {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
    }


}