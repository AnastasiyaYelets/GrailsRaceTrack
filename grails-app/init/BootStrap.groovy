import grails.util.Environment
import racetrack.Race
import racetrack.Runner
import racetrack.Registration
import racetrack.User
import racetrack.Book





import org.grails.plugins.excelimport.ExcelImportService
import org.grails.plugins.excelimport.*
import sample.*


class BookExcelImporter extends AbstractExcelImporter {

  static Map CONFIG_BOOK_CELL_MAP = [
     sheet:'Sheet2',
     cellMap: [ 'D3':'title',
        'D4':'author',
        'D6':'numSold',
	  ]
  ]

  static Map CONFIG_BOOK_COLUMN_MAP = [
          sheet:'Sheet1',
			 startRow: 2,
          columnMap:  [
                  'B':'title',
                  'C':'author',
                  'D':'numSold',
          ]
  ]

  public BookExcelImporter(fileName) {
    super(fileName)
  }

  List<Map> getBooks() {
    List bookList = ExcelImportUtilsService.convertColumnMapConfigManyRows(workbook, CONFIG_BOOK_COLUMN_MAP)
  }


  Map getOneMoreBookParams() {
    Map bookParams = ExcelImportService.convertFromCellMapToMapWithValues(workbook, CONFIG_BOOK_CELL_MAP )
  }

}




class BootStrap {

    def init = { servletContext ->

    //String fileName = "c:\\dev\\HEAD\\plugins\\excel-import\\test\\projects\\sample\\test-data\\books.xls"
     String fileName = "C:\\dev\\books.xls"
	 BookExcelImporter importer = new BookExcelImporter(fileName);

	 def booksMapList = importer.getBooks();
	 println booksMapList

	 booksMapList.each { Map bookParams ->
	  def newBook = new Book(bookParams)
	  if (!newBook.save()) {
	    println "Book not saved, errors = ${newBook.errors}"
	  }
	 }

    new Book(importer.getOneMoreBookParams()).save()

	 }
}
