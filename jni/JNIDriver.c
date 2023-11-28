#include <jni.h>
#include <fcntl.h>

int fd = 0;
int led_fd = 0;
int fnd_fd = 0;
int temp_fd = 0;

JNIEXPORT jint JNICALL Java_com_example_jnidriver_JNIDriver_openDriver(JNIEnv * env, jclass class, jstring path) {
	jboolean iscopy;
	const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
	fd = open(path_utf, O_WRONLY);
	(*env)->ReleaseStringUTFChars(env, path, path_utf);

	if (fd < 0) return -1;
	else return 1;
}

JNIEXPORT jint JNICALL Java_com_example_jnidriver_JNIDriver_openDriverLED(JNIEnv * env, jclass class, jstring path) {
	jboolean iscopy;
	const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
	led_fd = open(path_utf, O_WRONLY);
	(*env)->ReleaseStringUTFChars(env, path, path_utf);

	if (led_fd < 0) return -1;
	else return 1;
}

JNIEXPORT jint JNICALL Java_com_example_jnidriver_JNIDriver_openDriverFND(JNIEnv * env, jclass class, jstring path) {
	jboolean iscopy;
	const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
	fnd_fd = open(path_utf, O_WRONLY);
	(*env)->ReleaseStringUTFChars(env, path, path_utf);

	if (fnd_fd < 0) return -1;
	else return 1;
}

JNIEXPORT jint JNICALL Java_com_example_jnidriver_JNIDriver_openDriverTemp(JNIEnv * env, jclass class, jstring path) {
	jboolean iscopy;
	const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
	temp_fd = open(path_utf, O_WRONLY);
	(*env)->ReleaseStringUTFChars(env, path, path_utf);

	if (temp_fd < 0) return -1;
	else return 1;
}

JNIEXPORT void JNICALL Java_com_example_jnidriver_JNIDriver_closeDriver(JNIEnv * env, jobject obj) {
	if (fd > 0) close(fd);
}

JNIEXPORT void JNICALL Java_com_example_jnidriver_JNIDriver_closeDriverLED(JNIEnv * env, jobject obj) {
	if (led_fd > 0) close(led_fd);
}

JNIEXPORT void JNICALL Java_com_example_jnidriver_JNIDriver_closeDriverFND(JNIEnv * env, jobject obj) {
	if (fnd_fd > 0) close(fnd_fd);
}

JNIEXPORT void JNICALL Java_com_example_jnidriver_JNIDriver_closeDriverTemp(JNIEnv * env, jobject obj) {
	if (temp_fd > 0) close(temp_fd);
}

JNIEXPORT void JNICALL Java_com_example_jnidriver_JNIDriver_writeDriverLED(JNIEnv * env, jobject obj, jbyteArray arr, jint count) {
	jbyte* chars = (*env)->GetByteArrayElements(env, arr, 0);
	if (led_fd>0) write(led_fd, (unsigned char*)chars, count);
	(*env)->ReleaseByteArrayElements(env, arr, chars, 0);
}

JNIEXPORT void JNICALL Java_com_example_jnidriver_JNIDriver_writeDriverFND(JNIEnv * env, jobject obj, jbyteArray arr, jint count) {
	jbyte* chars = (*env)->GetByteArrayElements(env, arr, 0);
	if (fnd_fd>0) write(fnd_fd, (unsigned char*)chars, count);
	(*env)->ReleaseByteArrayElements(env, arr, chars, 0);
}

JNIEXPORT jfloat JNICALL Java_com_example_jnidriver_JNIDriver_readTemp(JNIEnv * env, jobject obj) {
	int iResult = 0;
	float t = 0, t_C = 0;

	if(temp_fd>0) {
		iResult = ioctl(temp_fd, 0, NULL);
		t = (float)iResult;
		t_C = -46.85+175.72/65536 * t;
		return t_C;
	}
	return 0;
}

JNIEXPORT void JNICALL Java_com_example_jnidriver_JNIDriver_setMotor(JNIEnv * env, jobject o, jchar c) {
	int i = (int) c;
	int k;

	char temp[4] = {1,1,1,1};

	if (i==0)
		write(fd,temp,4);
	else if (i==1) {
		temp[0] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[0] = 1;
		temp[1] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[1] = 1;
		temp[2] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[2] = 1;
		temp[3] = 0;
		write(fd,temp,4);
		usleep(10000);
		temp[3] = 1;
	} else if (i==2) {
		temp[0] = 0;
		temp[1] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[0] = 1;
		temp[2] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[1] = 1;
		temp[3] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[0] = 0;
		temp[2] = 1;
		write(fd,temp,4);
		usleep(10000);
		temp[0] = 1;
		temp[3] - 1;
	} else if (i==3) {
		temp[0] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[1] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[0] = 1;
		write(fd,temp,4);
		usleep(10000);

		temp[2] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[1] = 1;
		write(fd,temp,4);
		usleep(10000);

		temp[3] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[2] = 1;
		write(fd,temp,4);
		usleep(10000);

		temp[0] = 0;
		write(fd,temp,4);
		usleep(10000);

		temp[0] = 1;
		temp[3] = 1;
		write(fd,temp,4);
		usleep(10000);
	} else if (i==4) {

		for (k=0; k<125; k++) {

			temp[0] = 0;
			write(fd,temp,4);
			usleep(10000);

			temp[0] = 1;
			temp[1] = 0;
			write(fd,temp,4);
			usleep(10000);

			temp[1] = 1;
			temp[2] = 0;
			write(fd,temp,4);
			usleep(10000);

			temp[2] = 1;
			temp[3] = 0;
			write(fd,temp,4);
			usleep(10000);
			temp[3] = 1;

		} for (k=0; k<125; k++) {

			temp[3] = 0;
			write(fd,temp,4);
			usleep(10000);

			temp[3] = 1;
			temp[2] = 0;
			write(fd,temp,4);
			usleep(10000);

			temp[2] = 1;
			temp[1] = 0;
			write(fd,temp,4);
			usleep(10000);

			temp[1] = 1;
			temp[0] = 0;
			write(fd,temp,4);
			usleep(10000);

			temp[0] = 1;
			temp[1] = 1;
			temp[2] = 1;
			temp[3] = 1;
			usleep(10000);

		}
	}
}
