#include <SoftwareSerial.h>
#include <LiquidCrystal.h>
const byte rxPin = 2;
const byte txPin = 3;
SoftwareSerial esp8266(rxPin, txPin);

unsigned long lastTimeMillis = 0;
//***********lcd
/*
  LiquidCrystal Library - Hello World

  Demonstrates the use a 16x2 LCD display.  The LiquidCrystal
  library works with all LCD displays that are compatible with the
  Hitachi HD44780 driver. There are many of them out there, and you
  can usually tell them by the 16-pin interface.

  This sketch prints "Hello World!" to the LCD
  and shows the time.

  The circuit:
   LCD RS pin to digital pin 12
   LCD Enable pin to digital pin 11
   LCD D4 pin to digital pin 5
   LCD D5 pin to digital pin 4
   LCD D6 pin to digital pin 3
   LCD D7 pin to digital pin 2
   LCD R/W pin to ground
   LCD VSS pin to ground
   LCD VCC pin to 5V
   10K resistor:
   ends to +5V and ground
   wiper to LCD VO pin (pin 3)

  This example code is in the public domain.

*/
const int rs = 9, en = 8, d4 = 7, d5 = 6, d6 = 5, d7 = 4;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
//************buzzer
const int buzzer = 10;
const int led = 11;
void setup()
{
  Serial.begin(115200);
  esp8266.begin(115200);
  delay(2000);
  pinMode(A0, INPUT);
  pinMode(A1, INPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(led, OUTPUT);
  lcd.begin(16, 2);
  // Print a message to the LCD.
  lcd.print("HELLO");

  esp8266.println("AT+RST");
  Serial.println("AT+RST");
  delay(5000);
  printResponse();
  esp8266.println("AT+CWMODE=1");
  Serial.println("AT+CWMODE=1");
  delay(2000);
  printResponse();
  esp8266.println("AT+CWJAP=\"admin\",\"admin123\"");
  Serial.println("AT+CWJAP=\"admin\",\"admin123\"");
  delay(5000);
  printResponse();
  esp8266.println("AT+CIPSTA=\"192.168.43.100\"");
  Serial.println("AT+CIPSTA=\"192.168.43.100\"");
  delay(8000);
  printResponse();
  esp8266.println("AT+CIFSR");
  Serial.println("AT+CIFSR");
  delay(8000);
  printResponse();
  // put your setup code here, to run once:
}
void printResponse()
{
  while (esp8266.available()) {
    Serial.write(esp8266.read());
    //Serial.write(Serial.read());
  }
}
int i = 0, j = 0;
void loop()
{
  i = analogRead(A0);
  j = analogRead(A1);
  Serial.println(i);
  Serial.println(j);
  //******************************esp send and recive

  if (millis() - lastTimeMillis > 15)
  {
    lastTimeMillis = millis();

    esp8266.println("AT+CIPMUX=0");
    Serial.println("AT+CIPMUX=0");
    delay(1000);
    printResponse();
    esp8266.println("AT+CIPSTART=\"TCP\",\"192.168.43.152\",8080");
    Serial.println("AT+CIPSTART=\"TCP\",\"192.168.43.152\",8080");
    delay(1000);
    printResponse();
    if (i < 50)
    {
      esp8266.println("AT+CIPSTART=\"TCP\",\"192.168.43.152\",8080");
      Serial.println("AT+CIPSTART=\"TCP\",\"192.168.43.152\",8080");
      delay(1000);
      printResponse();
      String cmd = "GET /MediSmart/GetEventServlet?userId=1&slotId=1";
      esp8266.println("AT+CIPSEND=" + String(cmd.length() + 4));
      delay(1000);
      esp8266.println(cmd);
      esp8266.println("");
    }
    if (j < 50)
    {
      esp8266.println("AT+CIPSTART=\"TCP\",\"192.168.43.152\",8080");
      Serial.println("AT+CIPSTART=\"TCP\",\"192.168.43.152\",8080");
      delay(1000);
      printResponse();
      String cmd = "GET /MediSmart/GetEventServlet?userId=1&slotId=2";
      esp8266.println("AT+CIPSEND=" + String(cmd.length() + 4));
      delay(1000);
      esp8266.println(cmd);
      esp8266.println("");
    }
    esp8266.println("AT+CIPSTART=\"TCP\",\"192.168.43.152\",8080");
    Serial.println("AT+CIPSTART=\"TCP\",\"192.168.43.152\",8080");
    delay(1000);
    printResponse();
    String cmd = "GET /MediSmart/AlertAPI";
    esp8266.println("AT+CIPSEND=" + String(cmd.length() + 4));
    delay(1000);
    esp8266.println(cmd);
    esp8266.println("");

    int waiter = 0;
    String str = "";
    char incomingByte;
    Serial.println("String -----------------   ");
    if (esp8266.available())
    {
      incomingByte = esp8266.read();
      str += incomingByte;
      incomingByte = esp8266.read();
      str += incomingByte;
      incomingByte = esp8266.read();
      str += incomingByte;
    }
    Serial.println(str);
    while (incomingByte != '$') {
      incomingByte = esp8266.read();
      if (incomingByte == '*')
      {
        Serial.println("* taken next point need ");
        incomingByte = esp8266.read();
        if (incomingByte == 'A')
        {
          Serial.println("A taken next point need ");
          lcd.setCursor(0, 1);
          // print the number of seconds since reset:
          lcd.print("crosin Tablet");
          Serial.println("Buzzer on");

          int buzcount = 0;
          analogWrite(buzzer, HIGH);
          analogWrite(led, HIGH);
          while (i > 50 && j > 50)
          {
            delay(100);
            i = analogRead(A0);
            j = analogRead(A1);
            buzcount++;
            if (buzcount > 50)
            {
              buzcount = 0;
              analogWrite(buzzer, LOW);
              analogWrite(led, LOW);
              delay(2000);
              analogWrite(buzzer, HIGH);
              analogWrite(led, HIGH);
              while (i > 50 && j > 50)
              {
                delay(100);
                i = analogRead(A0);
                j = analogRead(A1);
                buzcount++;
                if (buzcount > 100)
                {
                  buzcount = 0;
                  analogWrite(buzzer, LOW);
                  analogWrite(led, LOW);
                  delay(2000);
                  analogWrite(buzzer, HIGH);
                  analogWrite(led, HIGH);
                  while (i > 50 && j > 50)
                  {
                    delay(100);
                    i = analogRead(A0);
                    j = analogRead(A1);
                    buzcount++;
                    if (buzcount > 150)
                    {
                      break;
                    }
                  }
                  break;
                }
              }
              break;
            }
          }
          analogWrite(buzzer, LOW);
          analogWrite(led, LOW);
        }


        if (incomingByte == 'B')
        {
          Serial.println("B taken next point need ");
          lcd.setCursor(0, 1);
          // print the number of seconds since reset:
          lcd.print("Maxiliv Tablet");
          Serial.println("Buzzer on");

          int buzcount = 0;
          analogWrite(buzzer, HIGH);
          analogWrite(led, HIGH);
          while (i > 50 && j > 50)
          {
            delay(100);
            i = analogRead(A0);
            j = analogRead(A1);
            buzcount++;
            if (buzcount > 50)
            {
              buzcount = 0;
              analogWrite(buzzer, LOW);
              analogWrite(led, LOW);
              delay(2000);
              analogWrite(buzzer, HIGH);
              analogWrite(led, HIGH);
              while (i > 50 && j > 50)
              {
                delay(100);
                i = analogRead(A0);
                j = analogRead(A1);
                buzcount++;
                if (buzcount > 100)
                {
                  buzcount = 0;
                  analogWrite(buzzer, LOW);
                  analogWrite(led, LOW);
                  delay(2000);
                  analogWrite(buzzer, HIGH);
                  analogWrite(led, HIGH);
                  while (i > 50 && j > 50)
                  {
                    delay(100);
                    i = analogRead(A0);
                    j = analogRead(A1);
                    buzcount++;
                    if (buzcount > 150)
                    {
                      break;
                    }
                  }
                  break;
                }
              }
              break;
            }
          }
          analogWrite(buzzer, LOW);
        }

        incomingByte = esp8266.read();
        //Serial.println(str);
        if (incomingByte == '$')
        {
          Serial.println("$ taken next loop will begin ");
          lcd.setCursor(0, 1);
          // print the number of seconds since reset:
          lcd.print("");
          break;
        }
      }

      waiter++;
      delay(1);
      if (waiter > 500)
      {
        break;
      }
    }
  }
}
