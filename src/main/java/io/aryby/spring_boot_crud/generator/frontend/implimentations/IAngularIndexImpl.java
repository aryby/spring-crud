package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.generator.frontend.IAngularIndex;
import org.springframework.stereotype.Service;

@Service
public class IAngularIndexImpl implements IAngularIndex {
    @Override
    public String generateAngularIndex(Long projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
            <!DOCTYPE html>
            <html lang="en" dir="ltr">
                <head>
                    <meta charset="utf-8" />
                    <base href="/" />
                    <meta name="viewport" content="width=device-width, initial-scale=1" />
                    <!-- Favicon -->
                    <link rel="icon" type="icon" href="assets/images/favicon.png" />
                    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
                </head>
                <body>
                    <app-root> </app-root>
                </body>
            </html>


            """);
        return sb.toString();
    }
}
