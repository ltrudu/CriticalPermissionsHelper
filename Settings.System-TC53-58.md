#List of Settings that can be read or written with the WRITE_SETTINGS permission on TC53/58 Android 14 (BSP: 14-28-03.00-UG-U60-STD-ATH-04)

| Description     | Data Type                            | Default Value                                               |
| --------------- | ------------------------------------ | ----------------------------------------------------------- |
| Display         | `accelerometer_rotation`             | Auto-rotate screen based on orientation                     | int | 1 (enabled) |
|                 | `dim_screen`                         | Dim screen in battery saver mode (deprecated in later APIs) | int | 1 (enabled) |
|                 | `display_default_density_value`      | Default screen pixel density (dpi)                          | int | Device-specific (e.g., 480) |
|                 | `display_default_resolution_height`  | Default screen height in pixels                             | int | Device-specific (e.g., 2160) |
|                 | `display_default_resolution_width`   | Default screen width in pixels                              | int | Device-specific (e.g., 1080) |
|                 | `font_scale`                         | System font size multiplier                                 | float | 1.0 (100%) |
|                 | `min_refresh_rate`                   | Minimum display refresh rate (Hz)                           | float | Device-specific (e.g., 60.0) |
|                 | `peak_refresh_rate`                  | Maximum display refresh rate (Hz)                           | float | Device-specific (e.g., 60.0) |
|                 | `screen_auto_brightness_adj`         | Auto-brightness adjustment factor (-1.0 to 1.0)             | float | 0.0 (neutral) |
|                 | `screen_brightness`                  | Manual screen brightness (0-255)                            | int | ~102 (40%) |
|                 | `screen_brightness_for_vr`           | Brightness level for VR mode                                | int | ~86 (34%) |
|                 | `screen_brightness_mode`             | Brightness control mode (0=manual, 1=auto)                  | int | 1 (auto) |
|                 | `screen_off_timeout`                 | Time until screen sleeps (ms)                               | int | 60000 (1 min) |
|                 | `user_rotation`                      | Forced screen orientation (0=0°, 1=90°, 2=180°, 3=270°)     | int | 0 (portrait) |
| Sound           | `alarm_alert`                        | Alarm sound URI                                             | string | Device-specific |
|                 | `call_connected_tone_enabled`        | Play tone when call connects                                | int | 0 (disabled) |
|                 | `dtmf_tone`                          | Play tones when dialing                                     | int | 0 (disabled) |
|                 | `dtmf_tone_type`                     | DTMF tone type (0=normal, 1=long)                           | int | 0 |
|                 | `lockscreen_sounds_enabled`          | Play sounds when unlocking                                  | int | 1 (enabled) |
|                 | `mode_ringer_streams_affected`       | Streams affected by ringer mode (bitmask)                   | int | 166 |
|                 | `mute_streams_affected`              | Streams that can be muted (bitmask)                         | int | 111 |
|                 | `notification_sound`                 | Notification sound URI                                      | string | Device-specific |
|                 | `ringtone`                           | Incoming call ringtone URI                                  | string | Device-specific |
|                 | `sound_effects_enabled`              | UI interaction sounds                                       | int | 1 (enabled) |
|                 | `vibrate_when_ringing`               | Vibrate on incoming call                                    | int | 1 (enabled) |
| Volume          | `volume_alarm`                       | Alarm volume level                                          | int | 6 |
|                 | `volume_bluetooth_sco`               | Bluetooth call volume                                       | int | 7 |
|                 | `volume_music`                       | Media volume                                                | int | 5 |
|                 | `volume_music_ble_broadcast`         | BLE broadcast media volume                                  | int | 3 |
|                 | `volume_music_ble_headset`           | BLE headset media volume                                    | int | 3 |
|                 | `volume_music_bt_a2dp_hp`            | Bluetooth A2DP headphone volume                             | int | 3 |
|                 | `volume_music_speaker`               | Speaker media volume                                        | int | 9 |
|                 | `volume_music_usb_headset`           | USB headset media volume                                    | int | 3 |
|                 | `volume_notification`                | Notification volume                                         | int | 5 |
|                 | `volume_notification_speaker`        | Speaker notification volume                                 | int | 2 |
|                 | `volume_ring`                        | Ringer volume                                               | int | 5 |
|                 | `volume_ring_speaker`                | Speaker ringer volume                                       | int | 2 |
|                 | `volume_system`                      | System sounds volume                                        | int | 7 |
|                 | `volume_voice`                       | Voice call volume                                           | int | 4 |
| Haptics         | `haptic_feedback_enabled`            | Vibrate on touch interactions                               | int | 1 (enabled) |
| Notification    | `charging_led_notification`          | Show LED while charging                                     | int | 1 (enabled) |
|                 | `nled_brightness_value`              | Notification LED brightness (0-255)                         | int | 255 (max) |
|                 | `notification_light_pulse`           | Pulse LED for notifications                                 | int | 1 (enabled) |
| Input           | `keylight_auto_brightness`           | Auto-adjust keyboard backlight                              | int | 1 (enabled) |
|                 | `keylight_brightness`                | Keyboard backlight brightness (0-255)                       | int | 51 |
|                 | `pointer_speed`                      | Mouse/trackpad speed (-7 to 7)                              | int | 0 (neutral) |
|                 | `touchkeylight_off_timeout`          | Time until touchkey lights turn off (ms)                    | int | 6000 (6 sec) |
| System          | `apply_ramping_ringer`               | Gradually increase ringer volume                            | int | 0 (disabled) |
|                 | `end_button_behavior`                | Power button action (2=sleep, 1=end call, etc.)             | int | 2 (sleep) |
|                 | `system_locales`                     | System language/locale                                      | string | en-US |
|                 | `time_12_24`                         | Time format (12h/24h)                                       | string | 12 (12-hour format) |
|                 | `tty_mode`                           | TTY mode (0=off, 1=full, 2=HCO, 3=VCO)                      | int | 0 (off) |
| Connectivity    | `data_connection_state_flag`         | Mobile data connection state                                | int | 0 |
|                 | `ethernet_tracker_flag`              | Ethernet connection tracking                                | int | 0 |
|                 | `rfid_sleep_state`                   | RFID sleep state                                            | int | 0 |
|                 | `wifi_state_flag`                    | Wi-Fi connection state                                      | int | 0 |
| Device-Specific | `smart_default_density_values`       | Smart display density profiles (OEM-specific)               | string | e.g., "408,480,540" |
|                 | `smart_docking_densiy_on_dock_save`  | Docking density profile (OEM-specific)                      | int | 0 |
|                 | `smart_docking_resolution_dpi_value` | Docking resolution settings (OEM-specific)                  | string | e.g., "1920,1080,200,10" |
|                 | `smart_docking_resolution_setting`   | Active docking resolution profile (OEM-specific)            | int | 10 |
|                 | `xternal_display_id_connected`       | Connected external display ID                               | int | \-1 (none) |
|                 | `xternal_display_set_mirror`         | Mirror display on external screen                           | int | 0 (disabled) |
|                 | `xternal_display_taskbar_reset`      | Reset taskbar on external display                           | int | 0 (disabled) |
| Flags           | `alarm_alert_set`                    | Alarm sound configured flag                                 | int | 1 (configured) |
|                 | `notification_sound_set`             | Notification sound configured flag                          | int | 1 (configured) |
|                 | `ringtone_set`                       | Ringtone configured flag                                    | int | 1 (configured) |