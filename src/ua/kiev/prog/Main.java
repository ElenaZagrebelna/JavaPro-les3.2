//Написать класс TextContainer, который содержит в себе строку.
// С помощью механизма аннотаций указать
// 1) в какой файл должен сохраниться текст
// 2) метод, который выполнит сохранение.
// Написать класс Saver, который сохранит поле класса TextContainer в указанный файл.
//
//`@SaveTo(path=“c:\\file.txt”)`
//
// class Container {
//String text = “...”;
//
// @Saver
//public void save(..) {...}
//}

package ua.kiev.prog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.*;

public class Main {

    public static void main(String[] args) throws Exception {

        TextContainer textContainer = new TextContainer();
        Sever sever = new Sever();

        Class<?> cls = TextContainer.class;
        Class<?> cls2 = Sever.class;

        Method[] methods = cls2.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(SaveTo.class)) {
                    SaveTo saveTo = method.getAnnotation(SaveTo.class);

                    Field[] fields = cls.getDeclaredFields();
                    for (Field field : fields) {
                        String text1 = (String) field.get(textContainer);

                        method.invoke(sever, saveTo.path(), text1);
                        System.out.println("good");   // control world
                    }
                }
            }
        }
    }
}

class TextContainer {
    String text = "Name text";

    public TextContainer() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

class Sever {
    @SaveTo(path = "D:\\file.txt")
    public void save(String path, String str) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(str);
        fileWriter.flush();
        fileWriter.close();
    }
}
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@interface SaveTo {
    String path();
}
