# StudyDetector
YOLO v5를 이용하여 공부에 집중하도록 감시해주는 애플리케이션 개발 프로젝트입니다.

# Workflow
<img width="1510" alt="스크린샷 2022-06-09 오후 9 32 52" src="https://user-images.githubusercontent.com/69189272/175866642-40436e97-263c-4bb4-8dc0-0019806d39f9.png">
<img width="1482" alt="스크린샷 2022-06-09 오후 9 32 59" src="https://user-images.githubusercontent.com/69189272/175866695-53a165b2-d943-49d3-9154-b18e905cab16.png">
<img width="1490" alt="스크린샷 2022-06-09 오후 9 33 04" src="https://user-images.githubusercontent.com/69189272/175866731-04b31f8d-b75d-49df-bd1b-2103bf2a356a.png">


<br>

# 0. YOLO v5에 관하여
![MODEL DESCRIPTION](https://github.com/ultralytics/yolov5/releases/download/v1.0/model_comparison.png)
![MODEL DESCRIPTION2](https://github.com/ultralytics/yolov5/releases/download/v1.0/model_plot.png)

모바일 프로세서의 한계 때문에 s모델을 사용하였다. 
이 모델만 하더라도 avd에서 지연이 발생하는 것으로 보아 더 정확도가 높고 무거운 모델의 사용은 생각을 해봐야할 것 같다.

# 1. dependencies

    
- material

    ~~~kotlin
    implementation 'com.google.android.material:material:1.6.1'
    ~~~
    
    <br>

- calendar

    ~~~kotlin
    implementation 'com.prolificinteractive:material-calendarview:1.4.3'
    ~~~

    <br>

- room 

    ~~~kotlin
    implementation 'androidx.room:room-runtime:2.4.2'
    kapt 'androidx.room:room-compiler:2.4.2'
    ~~~

    <br>

- androidx camera

    ~~~kotlin
    implementation "androidx.camera:camera-core:1.0.0-alpha05"
    implementation "androidx.camera:camera-camera2:1.0.0-alpha05"
    ~~~

    카메라 모듈 커스텀

    <br>

- Pytorch

    ~~~kotlin
    implementation 'org.pytorch:pytorch_android_lite:1.10.0'
    implementation 'org.pytorch:pytorch_android_torchvision_lite:1.10.0'
    ~~~

    Pytorch model parameter를 불러와 Yolo v5를 사용하기 위해 pytorch 추가

    <br>

# 참고

- YOLO v5
  https://github.com/ultralytics/yolov5.git
