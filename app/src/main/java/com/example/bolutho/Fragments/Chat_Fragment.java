
package com.example.bolutho.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bolutho.Fragments.ModelClasses.Chat;
import com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter.ChatAdapter;
import com.example.bolutho.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Chat_Fragment extends Fragment {
    private RecyclerView recyclerView;
    public static final int SERVERPORT = 3004;
    public static final String SERVER_IP = "192.168.0.102";
    private ClientThread clientThread;
    private Thread thread;
    private Handler handler;
    private Socket socket;
    private int clientTextColor;
    private List<Chat> chatList = new ArrayList<>();
    private ImageButton send;
    private ChatAdapter mChatAdapter;
    private EditText messageInput;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat_, container, false);
        clientThread = new ClientThread();
        thread = new Thread(clientThread);
        thread.start();
        handler = new Handler();
        recyclerView =view.findViewById(R.id.messages_view);
        send =view.findViewById(R.id.btSend);
        messageInput =view.findViewById(R.id.editText);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendMessage();

            }
        });
        settingUpRecyclerView(chatList);


        return view;
    }
    public void onSendMessage() {
        String message = messageInput.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final Chat chat = new Chat(message, true);
            messageInput.setText("");
            mChatAdapter.add(chat);
            mChatAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(mChatAdapter.getItemCount());
            clientThread.sendMessage(message);
        }
    }
    public class ClientThread implements Runnable {
        private Socket socket;
        private BufferedReader input;

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);

                while (!Thread.currentThread().isInterrupted()) {

                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if (message!=null){
                        Chat chat = new Chat(message,  false);
                        mChatAdapter.add(chat);

                    }
                    mChatAdapter.notifyDataSetChanged();
                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        void sendMessage(final String message) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (socket != null) {
                            PrintWriter out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream())),
                                    true);
                            out.println(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    String getTime () {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != clientThread) {
            clientThread.sendMessage("Disconnect");
            clientThread = null;
        }
    }
    private void settingUpRecyclerView(List<Chat> msgList ){
        mChatAdapter=new ChatAdapter(getActivity(),msgList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mChatAdapter);}



}
