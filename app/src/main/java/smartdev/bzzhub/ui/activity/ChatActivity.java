package smartdev.bzzhub.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.MyPreferenceManager;
import smartdev.bzzhub.repository.chatmodel.Messages;
import smartdev.bzzhub.repository.chatmodel.Users;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.ui.adapter.ChatAdapter;
import smartdev.bzzhub.ui.base.BaseActivity;
import smartdev.bzzhub.util.Constant;

import static smartdev.bzzhub.repository.PreferenceKey.ARG_USER_LANGUAGE;
import static smartdev.bzzhub.util.FBStoreKey.KEY_FLAG;
import static smartdev.bzzhub.util.FBStoreKey.KEY_GROUP;
import static smartdev.bzzhub.util.FBStoreKey.KEY_INDIVIDUAL_MESSAGE;
import static smartdev.bzzhub.util.FBStoreKey.KEY_MESSAGE;
import static smartdev.bzzhub.util.FBStoreKey.KEY_MESSAGE_CONTENT;
import static smartdev.bzzhub.util.FBStoreKey.KEY_SENDER_ID;
import static smartdev.bzzhub.util.FBStoreKey.KEY_TIME;
import static smartdev.bzzhub.util.FBStoreKey.KEY_USERS;

public class ChatActivity extends BaseActivity {


    @BindView(R.id.edittext_chat)
    EditText editTextChat;

    @BindView(R.id.layout_send)
    LinearLayout layoutSend;

    String content;
    int communityID;
    String title = "";

    FieldValue timeStamp;
    ArrayList<Messages> chat_messages = new ArrayList<>();
    private ChatAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    ListenerRegistration messageListenerRegistration;
    ListenerRegistration userListListenerRegistration;
    ArrayList<String> messages_ids = new ArrayList<>();

    ArrayList<Integer> memberIDs = new ArrayList<>();
    ArrayList<Users> members = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        communityID = getIntent().getIntExtra("community_id",0);
        title = getIntent().getStringExtra("community_name");

        tvTitle.setText(title);
        getMessages();
    }

    @OnClick(R.id.layout_send)
    void onSendClicked(){
        layoutSend.setClickable(false);
        layoutSend.setFocusable(false);

        int flag = 0;
        if (Constant.getInstance().getLoginFlag() == 1){
            flag = 1;
        }else {
            flag = 0;
        }
        timeStamp = FieldValue.serverTimestamp();
        content = editTextChat.getText().toString().trim();
        BzzApp.getBzHubRepository().chatInGroup(communityID,Constant.getInstance().getLoginID(),flag,content)
                .subscribe(new Observer<SimpleResponse>() {
                    Disposable mDispose;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDispose = d;
                    }

                    @Override
                    public void onNext(SimpleResponse simpleResponse) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        mDispose.dispose();
                    }
                });


        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put(KEY_TIME, timeStamp);
        taskMap.put(KEY_FLAG, Constant.getInstance().getLoginFlag());
        taskMap.put(KEY_SENDER_ID,Constant.getInstance().getLoginID());
        taskMap.put(KEY_MESSAGE_CONTENT,content);
        WriteBatch batch =  BzzApp.getFirestore().batch();

        editTextChat.setText("");
        batch.set( BzzApp.getFirestore().collection(KEY_MESSAGE).document(String.valueOf(communityID)).collection(KEY_INDIVIDUAL_MESSAGE)
                .document(), taskMap);
        batch.commit().addOnCompleteListener(task -> {
            layoutSend.setClickable(true);
            layoutSend.setFocusable(true);
            if (task.isSuccessful()) {

            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMessages() {
        chat_messages.clear();
        initRecyclerView();
        if (messageListenerRegistration != null) {
            messageListenerRegistration.remove();
            messageListenerRegistration = null;
        }

        if (userListListenerRegistration != null) {
            userListListenerRegistration.remove();
            userListListenerRegistration = null;
        }
        BzzApp.getFirestore().collection(KEY_MESSAGE).document(String.valueOf(communityID)).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        messageListenerRegistration =  BzzApp.getFirestore().collection(KEY_MESSAGE).document(String.valueOf(communityID))
                                .collection(KEY_INDIVIDUAL_MESSAGE).orderBy(KEY_TIME).addSnapshotListener((queryDocumentSnapshots, e) -> {
                                    if (queryDocumentSnapshots != null) {
                                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                            switch (dc.getType()) {
                                                case ADDED:
                                                    onHandleAdded(dc);
                                                    break;
                                                case MODIFIED:
                                                    onHandleModified(dc);
                                                    break;
                                                case REMOVED:
                                                  //  onHandleRemoved();
                                                    break;
                                            }
                                        }
                                    }
                                });
                    }
                }
        );

        BzzApp.getFirestore().collection(KEY_GROUP).document(String.valueOf(communityID)).collection(KEY_USERS).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        userListListenerRegistration = BzzApp.getFirestore().collection(KEY_GROUP).document(String.valueOf(communityID))
                                .collection(KEY_USERS).addSnapshotListener((queryDocumentSnapshots, e) -> {
                                    if (queryDocumentSnapshots != null) {
                                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                            Users users = new Users(dc.getDocument());
                                            switch (dc.getType()) {
                                                case ADDED:
                                                    if (!memberIDs.contains(users.getUserID())){
                                                        memberIDs.add(users.getUserID());
                                                        members.add(users);
                                                    }
                                                if (adapter != null){
                                                    adapter.setUserList(members);
                                                    Log.d("memberCount",String.valueOf(members.size()));
                                                }
                                                    break;
                                                case MODIFIED:
                                                    if (memberIDs.contains(users.getUserID())){
                                                        int index = memberIDs.indexOf(users.getUserID());
                                                        if (members.get(index).getUserFlag() == users.getUserFlag()){
                                                            members.set(index,users);
                                                        }
                                                    }
                                                    if (adapter != null){
                                                        adapter.setUserList(members);
                                                        recyclerView.postDelayed(() -> {
                                                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                                        }, 100);
                                                        Log.d("memberCount",String.valueOf(members.size()));
                                                    }
                                                    break;
                                                case REMOVED:
                                                    if (memberIDs.contains(users.getUserID())) {
                                                        int index = memberIDs.indexOf(users.getUserID());
                                                        if (members.get(index).getUserFlag() == users.getUserFlag()) {
                                                            members.remove(index);
                                                            memberIDs.remove(index);
                                                            recyclerView.postDelayed(() -> {
                                                                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                                            }, 100);
                                                            Log.d("memberCount",String.valueOf(members.size()));
                                                        }
                                                    }
                                                    if (adapter != null){
                                                        adapter.setUserList(members);
                                                        recyclerView.postDelayed(() -> {
                                                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                                        }, 100);
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                });
                    }
                }
        );
    }

    private void onHandleAdded(DocumentChange dc) {
        Messages message = new Messages(dc.getDocument());
        if (!messages_ids.contains(dc.getDocument().getId())) {
            messages_ids.add(dc.getDocument().getId());
        }
        chat_messages.add(message);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void onHandleModified(DocumentChange dc) {
        if (messages_ids.contains(dc.getDocument().getId())) {
            int index = messages_ids.indexOf(dc.getDocument().getId());

            Messages message = new Messages(dc.getDocument());
            chat_messages.set(index, message);
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageListenerRegistration != null) {
            messageListenerRegistration.remove();
            messageListenerRegistration = null;
        }

        if (userListListenerRegistration != null){
            userListListenerRegistration.remove();
            userListListenerRegistration = null;
        }
    }

    private void initRecyclerView() {
        if (adapter == null) {
            adapter = new ChatAdapter(chat_messages,this,members);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.iv_back)
    void onBackClicked(){
        finish();
    }
}