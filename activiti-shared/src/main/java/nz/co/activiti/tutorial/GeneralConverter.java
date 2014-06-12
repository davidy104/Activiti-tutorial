package nz.co.activiti.tutorial;

public interface GeneralConverter<M, V> {

	M convert(V source) throws ConvertException;
}
