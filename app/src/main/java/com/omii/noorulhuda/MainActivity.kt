package com.omii.noorulhuda

import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.webViewClient = WebViewClient()
        web.webChromeClient = WebChromeClient()
        web.loadDataWithBaseURL(
            "https://noor-ul-huda.local/",
            """
            <!doctype html><html lang="ur" dir="rtl"><head><meta charset="utf-8">
            <meta name="viewport" content="width=device-width,initial-scale=1">
            <style>
            *{box-sizing:border-box}body{margin:0;background:#071d18;color:#f5eedb;font-family:serif}
            header{padding:28px 20px;text-align:center;background:linear-gradient(135deg,#0b392d,#145744)}
            h1{margin:0;color:#e7c879;font-size:30px}header p{margin:8px 0 0;color:#e6dfcb}
            nav{display:flex;gap:8px;padding:12px;position:sticky;top:0;background:#09271f}
            button{border:1px solid #b9954b;background:#123d30;color:#f5eedb;border-radius:12px;padding:12px 10px;font-size:16px;flex:1}
            main{padding:16px}.panel{background:#10372c;border:1px solid #856b36;border-radius:16px;padding:16px;margin:12px 0}
            .arabic{font-size:27px;line-height:1.9;text-align:center;color:#f4dfaa}
            .urdu{font-size:19px;line-height:1.8}.muted{color:#c7b98e}
            input{width:100%;padding:12px;border-radius:10px;border:1px solid #b9954b;background:#f7f0df;color:#13251e;font-size:16px}
            .item{padding:14px 4px;border-bottom:1px solid #426456}.item:last-child{border:0}
            </style></head><body>
            <header><h1>نور الہدیٰ</h1><p>Noor-ul-Huda • قرآن و حدیث</p></header>
            <nav><button onclick="show('quran')">قرآن</button><button onclick="show('hadith')">حدیث</button><button onclick="show('dua')">دعائیں</button></nav>
            <main id="content"><section class="panel"><div class="arabic">بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ</div><p class="urdu" style="text-align:center">اللہ کے نام سے جو نہایت مہربان، رحم فرمانے والا ہے۔</p></section>
            <section class="panel"><h2>خوش آمدید</h2><p class="urdu">قرآنِ کریم، احادیث اور روزمرہ دعائیں پڑھنے کے لیے اوپر سے انتخاب کریں۔</p></section></main>
            <script>
            const el=document.getElementById('content');
            async function show(which){
              if(which==='dua'){el.innerHTML='<section class="panel"><h2>روزمرہ دعائیں</h2><div class="item"><div class="arabic">رَبِّ زِدْنِي عِلْمًا</div><p class="urdu">اے میرے رب! میرے علم میں اضافہ فرما۔</p><small class="muted">سورۃ طٰہٰ 20:114</small></div><div class="item"><div class="arabic">رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً</div><p class="urdu">اے ہمارے رب! ہمیں دنیا میں بھلائی عطا فرما اور آخرت میں بھی بھلائی عطا فرما۔</p><small class="muted">سورۃ البقرۃ 2:201</small></div></section>';return}
              if(which==='quran'){el.innerHTML='<section class="panel"><h2>قرآنِ کریم</h2><input id="s" placeholder="سورت کا نام یا نمبر لکھیں"><div id="list" class="urdu">سورتوں کی فہرست لوڈ ہو رہی ہے…</div></section>';try{let d=await (await fetch('https://api.alquran.cloud/v1/surah')).json();let list=document.getElementById('list');list.innerHTML='';d.data.forEach(x=>{let a=document.createElement('div');a.className='item';a.textContent=x.number+'. '+x.name+' — '+x.englishName;a.onclick=()=>readSurah(x.number);list.appendChild(a)});document.getElementById('s').oninput=e=>{[...list.children].forEach(a=>a.style.display=a.textContent.includes(e.target.value)?'block':'none')}}catch(e){document.getElementById('list').textContent='انٹرنیٹ کنکشن چیک کریں۔'}return}
              if(which==='hadith'){el.innerHTML='<section class="panel"><h2>صحیح بخاری</h2><p class="urdu">حدیث کا ڈیٹا انٹرنیٹ سے حاصل کیا جاتا ہے۔</p><div id="h">حدیث کے لیے مستند API ڈیٹا دستیاب ہونے پر یہاں دکھایا جائے گا۔</div></section>'}
            }
            async function readSurah(n){el.innerHTML='<section class="panel"><button onclick="show(\'quran\')">واپس</button><div id="verses">آیات لوڈ ہو رہی ہیں…</div></section>';try{let d=await(await fetch('https://api.alquran.cloud/v1/surah/'+n+'/editions/quran-uthmani,ur.jalandhry')).json();let ar=d.data[0].ayahs,ur=d.data[1].ayahs;document.getElementById('verses').innerHTML=ar.map((a,i)=>'<div class="item"><div class="arabic">'+a.text+' ۝</div><p class="urdu">'+ur[i].text+'</p></div>').join('')}catch(e){document.getElementById('verses').textContent='آیات لوڈ نہیں ہو سکیں۔'}}
            </script></body></html>
            """.trimIndent(),
            "text/html",
            "UTF-8",
            null
        )
        setContentView(web)
    }
}
