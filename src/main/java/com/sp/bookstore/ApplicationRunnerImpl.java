package com.sp.bookstore;

import com.sp.bookstore.service.BookStoreService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private BookStoreService bookStoreService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bookStoreService.initialize();

        try {
            String transaction = args.getSourceArgs()[0];
            bookStoreService.performTransaction(transaction);
        } catch (ArrayIndexOutOfBoundsException arrayIndexExp) {
            log.error("Invalid parameters provided exception!");
        }
        catch (Exception ex) {
            log.error("There was error while executing program!", ex);
        }

        //
        // bookStoreService.printBookStatus();
    }
}
