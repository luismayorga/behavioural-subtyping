package be.ac.ua.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.CONSTRUCTOR,
	ElementType.METHOD,
	ElementType.FIELD})
public @interface invariant {
	String value();
}
