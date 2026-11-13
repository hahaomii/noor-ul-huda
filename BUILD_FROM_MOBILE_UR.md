# موبائل سے APK بنانے کا طریقہ

یہ پروجیکٹ ابھی سورس کوڈ ہے، APK نہیں۔ کمپیوٹر کے بغیر GitHub Actions کے ذریعے APK بنائی جا سکتی ہے۔

1. ZIP فائل کو فون میں ڈاؤن لوڈ کرکے Extract کریں۔ اندر `noor_ul_huda` فولڈر کھولیں۔
2. GitHub پر سائن اِن کریں اور `New repository` بنائیں، نام `noor-ul-huda` رکھیں، Public یا Private منتخب کریں۔
3. GitHub repository میں `Add file` > `Upload files` منتخب کریں۔ `noor_ul_huda` فولڈر کے اندر کی تمام فائلیں اور `.github` فولڈر سمیت اپ لوڈ کریں؛ فولڈر کے اندر موجود `.github/workflows/build-apk.yml` بھی لازماً شامل ہو۔ پھر `Commit changes` کریں۔
4. Repository کے `Actions` ٹیب میں `Build Noor-ul-Huda APK` workflow کھولیں۔ اگر خودکار طور پر شروع نہ ہو تو `Run workflow` دبائیں۔
5. Build مکمل ہونے پر اسی run کے نیچے `Artifacts` میں `Noor-ul-Huda-debug-apk` ڈاؤن لوڈ کریں۔ ZIP extract کریں؛ `app-debug.apk` انسٹال کریں۔ Android اگر اجازت مانگے تو صرف اسی فائل منیجر/براؤزر کو install unknown apps کی اجازت دیں جس سے APK کھول رہے ہیں۔

## ضروری نوٹ
- GitHub اکاؤنٹ اور انٹرنیٹ درکار ہیں۔ GitHub Actions کی مفت limits اکاؤنٹ/ریپوزٹری کے حساب سے لاگو ہو سکتی ہیں۔
- یہ Debug APK ہے، Play Store release نہیں۔ ابھی کسی حقیقی فون پر ٹیسٹ نہیں ہوئی۔
- قرآن و حدیث کے API ڈیٹا کو انٹرنیٹ درکار ہے۔ حدیث کے عربی/اردو متن کی نمبرنگ اور باہمی مطابقت اشاعت سے پہلے مستند نسخے سے لازماً چیک کریں۔
- GitHub پر فائلیں اپ لوڈ کرتے وقت `noor_ul_huda` فولڈر کے اندر کی فائلیں اپ لوڈ کریں، پورا فولڈر بطور ایک فائل نہیں۔ پوشیدہ `.github` فولڈر بھی شامل ہونا چاہیے۔
