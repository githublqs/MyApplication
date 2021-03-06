cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "id": "cordova-plugin-device.device",
        "file": "plugins/cordova-plugin-device/www/device.js",
        "pluginId": "cordova-plugin-device",
        "clobbers": [
            "device"
        ]
    },
    {
        "id": "cordova-plugin-splashscreen.SplashScreen",
        "file": "plugins/cordova-plugin-splashscreen/www/splashscreen.js",
        "pluginId": "cordova-plugin-splashscreen",
        "clobbers": [
            "navigator.splashscreen"
        ]
    },
    {
        "id": "cordova-plugin-statusbar.statusbar",
        "file": "plugins/cordova-plugin-statusbar/www/statusbar.js",
        "pluginId": "cordova-plugin-statusbar",
        "clobbers": [
            "window.StatusBar"
        ]
    },
    {
        "id": "ionic-plugin-keyboard.keyboard",
        "file": "plugins/ionic-plugin-keyboard/www/android/keyboard.js",
        "pluginId": "ionic-plugin-keyboard",
        "clobbers": [
            "cordova.plugins.Keyboard"
        ],
        "runs": true
    },{
              "file": "plugins/cordova-plugin-whitelist/whitelist.js",
              "id": "cordova-plugin-whitelist.whitelist",
              "runs": true
          },
     {
         "file":"plugin/bridgePlugin.js",
         "id":"org.cchao.cordovafragment.bridgePlugin",
         "clobbers": [
             "navigator.bridgePlugin"
         ]
     }
];
module.exports.metadata = 
// TOP OF METADATA
{
   //"cordova-plugin-console": "1.0.3",
    "cordova-plugin-device": "1.1.2",
    "cordova-plugin-splashscreen": "3.2.2",
    "cordova-plugin-statusbar": "2.1.3",
     "ionic-plugin-keyboard": "2.2.1",
    "cordova-plugin-whitelist": "1.2.0",
    "org.cchao.cordovafragment.bridgePlugin":"1.0.0"//,
    //"cordova-plugin-crosswalk-webview": "2.0.0"

};
// BOTTOM OF METADATA
});
