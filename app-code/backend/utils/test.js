const fs = require('fs');
const path = require('path');

async function downloadPDF(url, filename) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Error fetching PDF: ${response.statusText}`);
        }

        const pdfBuffer = await response.buffer();

        // Specify the path where you want to save the downloaded file
        const filePath = path.join(__dirname, filename);

        // Write the buffer to the file system
        fs.writeFileSync(filePath, pdfBuffer);

        console.log(`PDF downloaded successfully: ${filePath}`);
    } catch (error) {
        console.error('Error downloading PDF:', error);
    }
}

// Usage example
downloadPDF('https://www.redalyc.org/pdf/4655/465545872004.pdf', 'downloaded_file.pdf');
