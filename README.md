# SmartRequestPlugin
Ściągnij cały projekt pluginu z githuba 
Rozpakuj go

//INSTALACJA IOS

0. Aby plugin działał konieczne jest dodanie pluginu wspierającego swift PRZED dodaniem tego pluginu oraz przed wpisaniem komendy cordova platform add ios.
1. Dodawanie pluginu wspomagającego:(wpisać będąc w katalogu projektu cordova) cordova plugin add cordova-plugin-add-swift-support --save
2. Następnie wpisać: cordova platform add ios
3. Następnie dodanie pluginu głównego: plugman install --platform ios --project {ścieżka do /platforms/ios/} --plugin {ścieżka do pluginu} 3.1. Przykład : plugman install --platform ios --project C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\ios --plugin C:\Users\Jan.Mazurek\plugins\SmartRequestPlugin
4. Należy teraz uruchomić Xcode będąc wewnatrz projektu open ./platforms/ios/NAZWAPROJEKTU.xcworkspace/
5. w Xcode przejść do nazwaProjectu.xcodeproj. Wybrać okno targets, następnie Build Settings i w zakładce Swift Compiler - Language zmienić Swift Language Version na Swift 5
6. API: 
```javascript
		
		SmartRequestPlugin.getRequest('<URL>',(t)=> {console.log(t);},(e)=> {console.log(e);});
```
6.1 URL -> tu wpisz adres strony bez <>


//INSTALACJA ANDROID

0. instalacja plugman z npm: npm install -g plugman

1. instalacja pluginu komendą : plugman install --platform android --project <scieżka do folderu \platoforms\android w projekcie do którego ma być wgrany plugin> --plugin <ścieżka do folderu z pluginem>
2. Przykład : plugman install --platform android --project C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\android --plugin C:\Users\Jan.Mazurek\plugins\SmartRequestPlugin

5. Należy edytować plik index.js (\platforms\android\app\src\main\assets\www\js) dodając na początku funkcji onDeviceReady() : 
```javascript
		
		SmartRequestPlugin.getRequest('<URL>',(t)=> {console.log(t);},(e)=> {console.log(e);});
```
6. <URL> tu wpisz adres strony 
7. Powinno działać
