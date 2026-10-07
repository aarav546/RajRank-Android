package com.rajrank.app

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object {
        private const val URL="https://ckaqphngoumeuawowtdy.supabase.co"
        private const val KEY="sb_publishable_ab2T33AIRYiQfB_OARA9CQ_b0VCe3bH"
    }
    private val client=OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); showLogin() }

    private fun base(): LinearLayout = LinearLayout(this).apply {
        orientation=LinearLayout.VERTICAL; gravity=Gravity.CENTER; setPadding(50,40,50,40); setBackgroundColor(Color.WHITE)
    }

    private fun showLogin() {
        val root=base()
        val title=TextView(this).apply { text="RajRank"; textSize=32f; setTextColor(Color.rgb(23,70,162)); gravity=Gravity.CENTER }
        val sub=TextView(this).apply { text="Rajasthan CET • Mock Test"; textSize=17f; gravity=Gravity.CENTER }
        val mobile=EditText(this).apply { hint="Mobile Number"; inputType=InputType.TYPE_CLASS_PHONE }
        val pass=EditText(this).apply { hint="Password"; inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD }
        val login=Button(this).apply { text="LOGIN" }
        val create=Button(this).apply { text="CREATE ACCOUNT" }
        val status=TextView(this).apply { gravity=Gravity.CENTER; setPadding(0,15,0,0) }
        root.addView(title); root.addView(sub); root.addView(mobile); root.addView(pass); root.addView(login); root.addView(create); root.addView(status)
        setContentView(root)
        login.setOnClickListener {
            val m=mobile.text.toString().trim(); val p=pass.text.toString()
            if(m.length<10 || p.isEmpty()){ status.text="Mobile number और password भरें"; return@setOnClickListener }
            login.isEnabled=false; create.isEnabled=false; status.text="Login हो रहा है..."
            findEmail(m,status){ email -> if(email!=null) signIn(email,p,status,login,create) else {login.isEnabled=true;create.isEnabled=true} }
        }
        create.setOnClickListener { showRegister() }
    }

    private fun findEmail(mobile:String,status:TextView,done:(String?)->Unit) {
        val body=JSONObject().put("input_mobile",mobile).toString()
        val req=Request.Builder().url("$URL/rest/v1/rpc/get_email_by_mobile").post(body.toRequestBody("application/json".toMediaType()))
            .header("apikey",KEY).header("Authorization","Bearer $KEY").build()
        client.newCall(req).enqueue(object:Callback{
            override fun onFailure(c:Call,e:IOException){runOnUiThread{status.text="Network error";done(null)}}
            override fun onResponse(c:Call,r:Response){val b=r.body?.string().orEmpty(); if(!r.isSuccessful){runOnUiThread{status.text="Mobile lookup failed";done(null)}} else {val e=b.trim().trim('"');runOnUiThread{if(e.isEmpty()||e=="null"){status.text="Mobile number registered नहीं है";done(null)}else done(e)}}}
        })
    }

    private fun signIn(email:String,password:String,status:TextView,login:Button,create:Button){
        val body=JSONObject().put("email",email).put("password",password).toString()
        val req=Request.Builder().url("$URL/auth/v1/token?grant_type=password").post(body.toRequestBody("application/json".toMediaType()))
            .header("apikey",KEY).header("Content-Type","application/json").build()
        client.newCall(req).enqueue(object:Callback{
            override fun onFailure(c:Call,e:IOException){runOnUiThread{status.text="Network error";login.isEnabled=true;create.isEnabled=true}}
            override fun onResponse(c:Call,r:Response){val b=r.body?.string().orEmpty(); if(!r.isSuccessful){runOnUiThread{status.text="Login failed: गलत mobile/password";login.isEnabled=true;create.isEnabled=true}} else {val j=JSONObject(b); val id=j.optJSONObject("user")?.optString("id").orEmpty();runOnUiThread{showHome(id)}}}
        })
    }

    private fun showRegister(){
        val root=base()
        val title=TextView(this).apply{text="Create RajRank Account";textSize=28f;setTextColor(Color.rgb(23,70,162));gravity=Gravity.CENTER}
        val name=EditText(this).apply{hint="Full Name"}
        val mobile=EditText(this).apply{hint="Mobile Number";inputType=InputType.TYPE_CLASS_PHONE}
        val email=EditText(this).apply{hint="Email";inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS}
        val pass=EditText(this).apply{hint="Password";inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD}
        val create=Button(this).apply{text="CREATE ACCOUNT"}; val back=Button(this).apply{text="BACK TO LOGIN"}
        val status=TextView(this).apply{gravity=Gravity.CENTER;setPadding(0,15,0,0)}
        root.addView(title);root.addView(name);root.addView(mobile);root.addView(email);root.addView(pass);root.addView(create);root.addView(back);root.addView(status);setContentView(root)
        create.setOnClickListener{
            val n=name.text.toString().trim();val m=mobile.text.toString().trim();val e=email.text.toString().trim();val p=pass.text.toString()
            if(n.isEmpty()||m.length<10||!e.contains("@")||p.length<6){status.text="सभी जानकारी सही भरें";return@setOnClickListener}
            create.isEnabled=false;back.isEnabled=false;status.text="Account बन रहा है..."
            val body=JSONObject().put("email",e).put("password",p).put("data",JSONObject().put("full_name",n).put("mobile",m)).toString()
            val req=Request.Builder().url("$URL/auth/v1/signup").post(body.toRequestBody("application/json".toMediaType())).header("apikey",KEY).build()
            client.newCall(req).enqueue(object:Callback{
                override fun onFailure(c:Call,x:IOException){runOnUiThread{status.text="Network error";create.isEnabled=true;back.isEnabled=true}}
                override fun onResponse(c:Call,r:Response){runOnUiThread{if(r.isSuccessful)status.text="Account बन गया। अब Login करें।" else status.text="Account नहीं बना: email/mobile शायद पहले से registered है";create.isEnabled=true;back.isEnabled=true}}
            })
        }
        back.setOnClickListener{showLogin()}
    }

    private fun showHome(userId:String){
        val root=base();val title=TextView(this).apply{text="RajRank";textSize=34f;setTextColor(Color.rgb(23,70,162));gravity=Gravity.CENTER}
        val w=TextView(this).apply{text="Welcome to RajRank";textSize=22f;gravity=Gravity.CENTER}
        val info=TextView(this).apply{text="Login successful\n\nRajasthan CET Mock Test";textSize=17f;gravity=Gravity.CENTER}
        val logout=Button(this).apply{text="LOGOUT"};root.addView(title);root.addView(w);root.addView(info);root.addView(logout);setContentView(root);logout.setOnClickListener{showLogin()}
    }
}
