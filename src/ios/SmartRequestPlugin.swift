
import UserNotifications

@objc(GpsPlugin)
class GpsPlugin : CDVPlugin {
    let notificationCenter = UNUserNotificationCenter.current()
    let options: UNAuthorizationOptions = [.alert, .sound, .badge]
    
    
    
    override func pluginInitialize() {
        super.pluginInitialize()
        print("SmartRequestPlugin :: Inizjalizacja")
        
        //initializeLocationBest()
        //initializeLocationWorse()
        inistializeNotification()
        sendNotification(title: "SmartRequestPlugin", body: "Inicjalizacja poprawna")
    }
    func inistializeNotification() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { success, error in
            if success {
                print("SmartRequestPlugin :: Powiadomienia ustawione poprawnie!!")
            } else if let error = error {
                print(error.localizedDescription)
            }
        }
        notificationCenter.getNotificationSettings { (settings) in
          if settings.authorizationStatus != .authorized {
            print("SmartRequestPlugin :: Powiadomienia zakazane przez użytkownika")
          }
        }
        
    }
    func sendNotification(title: String,body:String) {
        let content = UNMutableNotificationContent()
        content.title = title
        content.subtitle = body
        content.sound = UNNotificationSound.default

        // show this notification five seconds from now
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 5, repeats: false)

        // choose a random identifier
        let request = UNNotificationRequest(identifier: UUID().uuidString, content: content, trigger: trigger)

        // add our notification request
        UNUserNotificationCenter.current().add(request)
        
    }
    
    @objc(getRequest:)
    func getRequest( command: CDVInvokedUrlCommand) {

        print("SmartRequestPlugin :: test is called")
        
        
            
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        commandDelegate.send(pluginResult, callbackId:command.callbackId)
    }
    @objc(ready:)
    func ready( command: CDVInvokedUrlCommand) {

        print("SmartRequestPlugin :: start is called")
        
        
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        commandDelegate.send(pluginResult, callbackId:command.callbackId)
    }
}

