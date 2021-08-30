# Chinese TTS TF Lite

### APP编译说明

1. Android 2020.3.1
2. 从下面的模型下载地址，下载models-tflite.7z，把如下4个文件解压到对应目录

```
├─app/src/main/assets/assets
│      baker_mapper.json
│      fastspeech2_quan.tflite
│      mb_melgan.tflite
│      tacotron2_quan.tflite
   ```

### 模型下载

[下载地址](https://github.com/benjaminwan/ChineseTtsTflite/releases/tag/init)

- models-tf.7z : 原始TensorflowTTS模型，一般用于PC端
- models-tflite.7z : 转换后的TFLite模型，一般用于移动端

### 模型查看

[netron](https://github.com/lutzroeder/netron/releases)

### 参考资料

[tensorflow指南](https://www.tensorflow.org/lite/guide/android)
[参考tf测试](https://colab.research.google.com/drive/1YpSHRBRPBI7cnTkQn1UcVTWEQVbsUm1S?usp=sharing)
[参考tflite转换](https://colab.research.google.com/drive/1Ma3MIcSdLsOxqOKcN1MlElncYMhrOg3J?usp=sharing)

### 环境配置

- Ubuntu: 20.04 LTS
- Python: 3.8

```shell
$ git clone https://github.com/TensorSpeech/TensorFlowTTS.git
$ cd TensorFlowTTS
$ pip install .
$ pip install git+https://github.com/repodiac/german_transliterate.git
```

### 模型测试&转换

解压models-tf.7z

1. TF模型测试

```shell
$ cd models-tf
$ python test-h5.py
```

2. TF模型转TFLite

```shell
$ python convert-tflite.py
```