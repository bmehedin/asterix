import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AsterixService {

  private asterixUrl: string;

  constructor(private http: HttpClient) {
    this.asterixUrl = 'http://localhost:8080/asterix';
  }

  public sendAsterixData(asterixIds: Set<string>, nbOfFlights: number, nbOfPositions: number) {

    const url = `${this.asterixUrl}/send-data/${nbOfFlights}/${nbOfPositions}`;
    return this.http.post<void>(url, Array.from(asterixIds))
  }
}
