import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AsterixService {

  private asterixUrl: string;

  constructor(private http: HttpClient) {
    this.asterixUrl = 'http://localhost:8080/asterix';
  }

  public sendAsterixData(asterixIds: Set<string>, nbOfFlights: number, nbOfPositions: number) {

    const url = `${this.asterixUrl}/${nbOfFlights}/${nbOfPositions}`;

    return this.http.post(url, Array.from(asterixIds), {
      responseType: 'blob',
      headers: new HttpHeaders({
        'Accept': 'application/zip'
      })
    });
  }
}
