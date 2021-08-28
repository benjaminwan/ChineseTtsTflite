# Chinese TTS TF Lite

### APP开发中……

### 模型下载

[下载地址](https://github.com/benjaminwan/ChineseTtsTflite/releases/tag/init)

- models-tf.7z : 原始TensorflowTTS模型
- models-tflite.7z : 转换后的TFLite模型

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

1. 模型测试

```shell
$ cd models-tf
$ python test-h5.py
```

2. 模型转换

```shell
$ python convert-tflite.py
```