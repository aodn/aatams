import org.junit.Test;

abstract class GrailsCrudTest extends TestBase 
{
	@Test
	abstract void testList()

	@Test
	abstract void testShow()

	@Test
	abstract void testCreate()

	@Test
	abstract void testEdit()

	@Test
	void testDelete()
	{
		// Implementations will generally test deletion as part of "testCreate()".
	}
}
