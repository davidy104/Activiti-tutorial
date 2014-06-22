package nz.co.activiti.tutorial.taskprocess.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["laptopName","modeNo"])
class LaptopModel implements Serializable {
	String laptopName
	String modeNo
	long qty = 0
	BigDecimal price = BigDecimal.ZERO
}
