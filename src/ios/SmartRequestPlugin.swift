
@objc(SmartRequestPlugin)
class SmartRequestPlugin : CDVPlugin {

    override func pluginInitialize() {
        super.pluginInitialize()
        print("SmartRequestPlugin :: Inizjalizacja")

    }

    @objc(getRequest:)
    func getRequest( command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,messageAs:"Nie podano URL!")
        //let param = command.argument(at: 0) as! String?
        let param = command.arguments[0] as? String
       // urll = command.arguments[0]
        if param == nil {
            print("NILLLLLLL")
            pluginResult?.keepCallback = false
            self.commandDelegate.send(pluginResult, callbackId:command.callbackId)
            return
        }
        let parame = param
        if parame?.isEmpty == true {
            pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,messageAs:"Zbyt krótki URL!")
            pluginResult?.keepCallback = false
            self.commandDelegate.send(pluginResult, callbackId:command.callbackId)
            return
        }
        print("SmartRequestPlugin :: test is called with \(param)")

        let url = URL(string: parame!)
        guard let requestUrl = url else { fatalError() }
        // Create URL Request
        var request = URLRequest(url: requestUrl)
        // Specify HTTP Method to use
        request.httpMethod = "GET"
        // Send HTTP Request

        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in

            // Check if Error took place
            if let error = error {
                print("Error took place \(error)")
                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,messageAs:"Error took place \(error)")
                pluginResult?.keepCallback = true
                self.commandDelegate.send(pluginResult, callbackId:command.callbackId)
                return
            }

            // Read HTTP Response Status code
            if let response = response as? HTTPURLResponse {
                print("Response HTTP Status code: \(response.statusCode)")
                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,messageAs:"Response HTTP Status code: \(response.statusCode)")
                pluginResult?.keepCallback = true
                self.commandDelegate.send(pluginResult, callbackId:command.callbackId)
            }

            // Convert HTTP Response Data to a simple String
            if let data = data, let dataString = String(data: data, encoding: .utf8) {
                print("Response data string:\n \(dataString)")
                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,messageAs:"Response data string:\n \(dataString)")
                pluginResult?.keepCallback = true
                self.commandDelegate.send(pluginResult, callbackId:command.callbackId)
            }



        }
        task.resume()


    }
    @objc(ready:)
    func ready( command: CDVInvokedUrlCommand) {

        print("SmartRequestPlugin :: start is called")
        
        
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        commandDelegate.send(pluginResult, callbackId:command.callbackId)
    }
}

