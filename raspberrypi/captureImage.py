from picamera.array import PiRGBArray
from picamera import PiCamera

import time
import cv2
import imutils

import os
import sys
import boto3
def main(argv):
        print (time.localtime(time.time()))
        s3 = boto3.client('s3',
                 aws_access_key_id = 'aws_access_key_id',
                 aws_secret_access_key = 'aws_secret_access_key')


        ### Setup #####################################################################

        # Center coordinates
        #cx = 160
        #cy = 120

        #os.system( "echo 0=150 > /dev/servoblaster" )
        #os.system( "echo 1=150 > /dev/servoblaster" )

        #xdeg = 150
        #ydeg = 150

        # Setup the camera
        camera = PiCamera()
        camera.resolution = ( 640, 480 )
        camera.framerate = 40
        rawCapture = PiRGBArray( camera, size=( 640, 480 ) )

        # Load a cascade file for detecting faces
        face_cascade = cv2.CascadeClassifier( 'haarcascade_frontalface_default.xml' )

        t_start = time.time()
        fps = 0
        
        


        ### Main ######################################################################

        # Capture frames from the camera
        for frame in camera.capture_continuous( rawCapture, format="bgr", use_video_port=True ):

            image = frame.array

            # Use the cascade file we loaded to detect faces
            gray = cv2.cvtColor( image, cv2.COLOR_BGR2GRAY )
            faces = face_cascade.detectMultiScale( gray )

           

            # Draw a rectangle around every face and move the motor towards the face
            for ( x, y, w, h ) in faces:
                cv2.imwrite("image%04i.jpeg", image)
                
                
                cv2.rectangle( image, ( x, y ), ( x + w, y + h ), ( 100, 255, 100 ), 2 )
                

                # Calculate and show the FPS
                #cv2.imwrite("image%04i.jpeg", image)
                    
                s3.upload_file('/home/pi/image%04i.jpeg','bucket1','image11.jpeg')
                time.sleep(10)    
            
            # Show the frame
            cv2.imshow( "Frame", image)
            


            
            cv2.waitKey( 1 )

            # Clear the stream in preparation for the next frame
            rawCapture.truncate( 0 )

if __name__ == '__main__':
        main(sys.argv)
